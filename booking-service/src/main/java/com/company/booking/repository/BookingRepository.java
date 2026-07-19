package com.company.booking.repository;

import com.company.booking.entity.Booking;
import com.company.booking.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * All three pagination flavours in one place so you can point to each in an interview:
 *   1) Page  - 2 queries (data + count). Use when a human reads "Page 3 of 47".
 *   2) Slice - 1 query (fetches size+1). Use for infinite scroll.
 *   3) List  - 1 query with keyset predicate. Use for feeds / deep pages.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // -----------------------------------------------------------------------
    // 1) OFFSET pagination with total count
    //    Generates:  SELECT ... LIMIT ? OFFSET ?     +     SELECT COUNT(*) ...
    //    Cost:       O(offset) for the data query,     +   full index scan for count
    // -----------------------------------------------------------------------
    Page<Booking> findByTenantId(Long tenantId, Pageable pageable);

    // -----------------------------------------------------------------------
    // 2) SLICE - infinite scroll, no count query
    //    Spring fetches size+1 rows; if the extra row is present hasNext()=true.
    // -----------------------------------------------------------------------
    Slice<Booking> findByTenantIdAndStatus(Long tenantId, BookingStatus status, Pageable pageable);

    // -----------------------------------------------------------------------
    // 3) OFFSET with dynamic optional filter (custom @Query MUST provide countQuery)
    // -----------------------------------------------------------------------
    @Query(
        value = """
            SELECT b FROM Booking b
             WHERE b.tenantId = :tenantId
               AND (:showId IS NULL OR b.showId = :showId)
            """,
        countQuery = """
            SELECT COUNT(b) FROM Booking b
             WHERE b.tenantId = :tenantId
               AND (:showId IS NULL OR b.showId = :showId)
            """)
    Page<Booking> search(@Param("tenantId") Long tenantId,
                         @Param("showId") Long showId,
                         Pageable pageable);

    // -----------------------------------------------------------------------
    // 4) KEYSET (cursor) pagination - flat cost, drift-free
    //
    //    ORDER BY (created_at DESC, id DESC) matches the composite index
    //    idx_booking_feed (tenant_id, created_at DESC, id DESC).
    //
    //    The tuple comparison (created_at, id) < (:cursorTs, :cursorId) is
    //    expanded manually because JPQL can't do row-value syntax:
    //        a < x  OR  (a = x  AND  b < y)
    // -----------------------------------------------------------------------
    @Query("""
        SELECT b FROM Booking b
         WHERE b.tenantId = :tenantId
           AND (:cursorTs IS NULL
                OR b.createdAt < :cursorTs
                OR (b.createdAt = :cursorTs AND b.id < :cursorId))
         ORDER BY b.createdAt DESC, b.id DESC
        """)
    List<Booking> findKeyset(@Param("tenantId") Long tenantId,
                             @Param("cursorTs")  Instant cursorTs,
                             @Param("cursorId")  Long    cursorId,
                             Pageable limit);

    // -----------------------------------------------------------------------
    // Safety net at the DB level - the app uses this before insert, and the
    // UNIQUE constraint uk_show_seat catches the race.
    // -----------------------------------------------------------------------
    Optional<Booking> findByShowIdAndSeatNo(Long showId, String seatNo);
}
