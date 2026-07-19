package com.company.booking.service;

import com.company.booking.entity.Booking;
import com.company.booking.entity.BookingStatus;
import com.company.booking.entity.Show;
import com.company.booking.event.BookingConfirmedEvent;
import com.company.booking.exception.BookingNotFoundException;
import com.company.booking.exception.SeatUnavailableException;
import com.company.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * THE SELF-INVOCATION FIX - this class exists for one precise reason.
 *
 * THE BUG IT FIXES:
 * BookingService.createBooking() originally called this.reservePending() and
 * this.markStatus() - both annotated @Transactional. But Spring transactions
 * are implemented as a PROXY wrapped around the bean. Calls that arrive from
 * OUTSIDE go through the proxy (tx opens); calls made via `this.` go straight
 * to the raw object - proxy skipped, NO TRANSACTION.
 *
 * Concretely: markStatus() loaded the entity, called setStatus(), and returned,
 * relying on Hibernate dirty-checking to flush the change at commit. With no
 * transaction there IS no commit - the status update was silently lost.
 *
 * THE RULE: @Transactional/@Cacheable/@Async only work across BEAN BOUNDARIES.
 * Moving these steps into their own bean means BookingService's calls cross a
 * proxy, and the annotations actually apply.
 */
@Component
@RequiredArgsConstructor
public class BookingTxSteps {

    private final BookingRepository bookingRepo;
    private final ApplicationEventPublisher events;

    /**
     * Phase 1: short write tx. Insert the seat reservation as PENDING.
     * The app-level check is best-effort; the DB UNIQUE(show_id, seat_no)
     * constraint is the real guarantee against a double-book race.
     */
    @Transactional
    public Booking reservePending(Long tenantId, Show show, Long userId, String seatNo) {
        bookingRepo.findByShowIdAndSeatNo(show.getId(), seatNo).ifPresent(b -> {
            throw new SeatUnavailableException("Seat " + seatNo + " already booked");
        });

        Booking booking = Booking.builder()
            .tenantId(tenantId)
            .showId(show.getId())
            .userId(userId)
            .seatNo(seatNo)
            .status(BookingStatus.PENDING)
            .priceCents(show.getPriceCents())
            .build();

        try {
            return bookingRepo.saveAndFlush(booking);
        } catch (DataIntegrityViolationException e) {
            throw new SeatUnavailableException("Seat " + seatNo + " just got booked");
        }
    }

    /**
     * Phase 2: fresh tx to finalize. Dirty-checking flushes setStatus() at commit.
     *
     * The CONFIRMED event is published INSIDE the tx, but the listener is
     * @TransactionalEventListener(AFTER_COMMIT) - it only fires if this commit
     * succeeds. Rollback = no notification. That ordering is the whole point.
     */
    @Transactional
    public Booking markStatus(Long bookingId, BookingStatus status) {
        Booking b = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException("Booking " + bookingId));
        b.setStatus(status);

        if (status == BookingStatus.CONFIRMED) {
            events.publishEvent(new BookingConfirmedEvent(
                b.getId(), b.getTenantId(), b.getUserId(), b.getShowId(), b.getSeatNo()));
        }
        return b;    // dirty-checked & flushed at commit - works BECAUSE we're in a real tx now
    }
}
