package com.company.booking.service;

import com.company.booking.dto.response.CursorPage;
import com.company.booking.entity.Booking;
import com.company.booking.entity.BookingStatus;
import com.company.booking.entity.Show;
import com.company.booking.repository.BookingRepository;
import com.company.booking.util.Cursor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrator. Collaborates with THREE other beans:
 *   - {@link ShowService}      cached show lookups
 *   - {@link PaymentService}   external side effect (kept OUTSIDE any tx)
 *   - {@link BookingTxSteps}   the transactional write steps
 *
 * WHY THE WRITE STEPS LIVE IN ANOTHER BEAN (BookingTxSteps):
 * An earlier version had reservePending()/markStatus() as @Transactional
 * methods on THIS class, called via `this.` - which bypasses the Spring proxy,
 * so no transaction ever started and the status update was silently lost to
 * a missing flush. Cross-bean calls go through the proxy; that's the fix.
 * Full explanation in BookingTxSteps' Javadoc.
 *
 * TRANSACTION SHAPE of createBooking (the "why not one big @Transactional?" answer):
 *   1. tx#1: insert PENDING row, commit          (short - minimal lock time)
 *   2. NO tx: call payment provider              (external effects can't roll back)
 *   3. tx#2: mark CONFIRMED/FAILED, commit       (fires AFTER_COMMIT event -> async notification)
 * A successful charge must never be undone by a DB rollback, and a held-open
 * tx during a slow network call would pin a connection + locks for the duration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepo;
    private final ShowService showService;         // cached reads
    private final PaymentService paymentService;   // external side effect
    private final BookingTxSteps txSteps;          // transactional writes (cross-bean = proxy applies)

    // =========================================================================
    // COMMAND: create a booking
    // =========================================================================
    public Booking createBooking(Long tenantId, Long showId, Long userId, String seatNo) {

        // Step 1: validate show exists (through the Caffeine cache)
        Show show = showService.getShow(tenantId, showId);

        // Step 2 (tx#1): reserve seat as PENDING
        Booking pending = txSteps.reservePending(tenantId, show, userId, seatNo);

        // Step 3 (no tx): payment provider
        PaymentService.PaymentResult result;
        try {
            result = paymentService.charge(userId, show.getPriceCents());
        } catch (RuntimeException e) {
            txSteps.markStatus(pending.getId(), BookingStatus.FAILED);
            throw e;
        }

        // Step 4 (tx#2): finalize; CONFIRMED publishes BookingConfirmedEvent AFTER_COMMIT
        return txSteps.markStatus(pending.getId(),
                result.success() ? BookingStatus.CONFIRMED : BookingStatus.FAILED);
    }

    // =========================================================================
    // QUERY: single booking (GET /bookings/{id})
    // =========================================================================
    /**
     * Tenant check is part of the lookup: a booking that exists but belongs to
     * another tenant is reported as NOT FOUND (404), never as 403 - we don't
     * even reveal that the id exists. Standard multi-tenant hygiene.
     */
    @Transactional(readOnly = true)
    public Booking getBooking(Long tenantId, Long bookingId) {
        return bookingRepo.findById(bookingId)
            .filter(b -> b.getTenantId().equals(tenantId))
            .orElseThrow(() -> new com.company.booking.exception.BookingNotFoundException(
                "Booking " + bookingId + " not found"));
    }

    // =========================================================================
    // COMMAND: cancel (DELETE /bookings/{id})
    // =========================================================================
    /**
     * SOFT delete: we flip status to CANCELLED instead of removing the row.
     * Real systems almost never hard-delete money-adjacent records - you lose
     * the audit trail and refund evidence. The seat frees up logically (a real
     * system would also release the uk_show_seat hold - kept simple here).
     * <p>
     * IDEMPOTENT by design: DELETE-ing an already-cancelled booking returns the
     * same result, not an error. HTTP DELETE semantics say repeating the call
     * must not change the outcome - clients retry on timeouts.
     */
    public Booking cancelBooking(Long tenantId, Long bookingId) {
        Booking b = getBooking(tenantId, bookingId);      // 404 + tenant check
        if (b.getStatus() == BookingStatus.CANCELLED) {
            return b;                                     // already done - idempotent
        }
        return txSteps.markStatus(b.getId(), BookingStatus.CANCELLED);
    }

    // =========================================================================
    // QUERY: offset pagination (Page)
    // =========================================================================
    /**
     * readOnly = true: Hibernate skips dirty-check snapshots, and the JDBC
     * connection is flagged read-only so a routing DataSource can use a replica.
     */
    @Transactional(readOnly = true)
    public Page<Booking> listBookings(Long tenantId, Pageable pageable) {
        return bookingRepo.findByTenantId(tenantId, pageable);
    }

    // =========================================================================
    // QUERY: keyset (cursor) pagination
    // =========================================================================
    // =========================================================================
    // QUERY: Slice pagination (no COUNT) + filtered search
    // These exist so every repository flavour is reachable through the API.
    // =========================================================================

    /** Slice = one query fetching size+1 rows; hasNext without a COUNT. */
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Slice<Booking> listByStatus(
            Long tenantId, BookingStatus status, Pageable pageable) {
        return bookingRepo.findByTenantIdAndStatus(tenantId, status, pageable);
    }

    /** Optional-filter search - demonstrates custom @Query with explicit countQuery. */
    @Transactional(readOnly = true)
    public Page<Booking> searchBookings(Long tenantId, Long showId, Pageable pageable) {
        return bookingRepo.search(tenantId, showId, pageable);
    }

    /** The "fetch size+1" probe gives hasMore without a COUNT query. */
    @Transactional(readOnly = true)
    public CursorPage<Booking> listByCursor(Long tenantId, String encodedCursor, int size) {
        Cursor cursor = Cursor.decode(encodedCursor);

        List<Booking> rows = new ArrayList<>(bookingRepo.findKeyset(
            tenantId,
            cursor == null ? null : cursor.createdAt(),
            cursor == null ? null : cursor.id(),
            PageRequest.of(0, size + 1)             // +1 = probe row
        ));

        boolean hasMore = rows.size() > size;
        String nextCursor = null;
        if (hasMore) {
            rows.remove(size);                       // discard probe
            Booking last = rows.get(rows.size() - 1);
            nextCursor = new Cursor(last.getCreatedAt(), last.getId()).encode();
        }
        return CursorPage.of(rows, nextCursor, hasMore);
    }
}
