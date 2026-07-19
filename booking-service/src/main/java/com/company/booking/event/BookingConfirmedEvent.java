package com.company.booking.event;

/**
 * Application event - published when a booking reaches CONFIRMED.
 *
 * WHY EVENTS:
 * BookingService should not know about email/SMS/analytics. Publishing an event
 * decouples "the booking happened" from "everything that reacts to it". New
 * reactions = new listeners; the publisher never changes. (In a microservice
 * world this same idea becomes Kafka/SQS messages - this is the in-process version.)
 *
 * Carries tenantId explicitly: listeners may run on a DIFFERENT THREAD (@Async),
 * where the TenantContext ThreadLocal of the request thread does not exist.
 * Never rely on ThreadLocals inside async listeners - pass data in the event.
 */
public record BookingConfirmedEvent(
    Long bookingId,
    Long tenantId,
    Long userId,
    Long showId,
    String seatNo
) {}
