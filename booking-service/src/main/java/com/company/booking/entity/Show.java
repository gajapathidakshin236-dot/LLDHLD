package com.company.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * A show/event you can book seats for.
 * Table name is {@code show_event} because {@code show} is a reserved keyword in some DBs.
 */
@Entity
@Table(name = "show_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Multi-tenant boundary - EVERY query filters on this. */
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String venue;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    /** Money in the smallest unit (cents/paise) - never store money as float. */
    @Column(name = "price_cents", nullable = false)
    private Long priceCents;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    /**
     * OPTIMISTIC LOCKING.
     * Two admins load the same show and both hit save. Without protection the
     * second save silently overwrites the first ("lost update").
     * <p>
     * {@code @Version} makes Hibernate append {@code WHERE version = <loaded value>}
     * to every UPDATE and increment the column. The second writer's WHERE matches
     * 0 rows -> Hibernate throws {@code ObjectOptimisticLockingFailureException}
     * -> our handler maps it to 409 Conflict, and the client retries with fresh data.
     * <p>
     * "Optimistic" because we don't lock rows up front (that's pessimistic,
     * SELECT FOR UPDATE) - we assume conflicts are rare and detect them at write.
     */
    @Version
    @Column(nullable = false)
    private Long version;
}
