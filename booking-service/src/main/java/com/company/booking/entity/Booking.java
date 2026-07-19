package com.company.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * A booking = one seat reservation for one show by one user.
 * <p>
 * Unique constraint on (show_id, seat_no) at DB level is the real
 * safeguard against double-booking. Even if two threads race past the
 * app-level "is this seat free?" check, the DB rejects the second insert
 * with a constraint violation - which the service maps to
 * {@link com.company.booking.exception.SeatUnavailableException}.
 */
@Entity
@Table(
    name = "booking",
    uniqueConstraints = @UniqueConstraint(name = "uk_show_seat", columnNames = {"show_id", "seat_no"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "show_id", nullable = false)
    private Long showId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "seat_no", nullable = false, length = 10)
    private String seatNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(name = "price_cents", nullable = false)
    private Long priceCents;

    /** Cursor key for keyset pagination (combined with id for tiebreak). */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (status == null)    status = BookingStatus.PENDING;
    }
}
