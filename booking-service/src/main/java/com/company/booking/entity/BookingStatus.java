package com.company.booking.entity;

/**
 * Explicit finite state - keep enums, not magic strings.
 * PENDING     - reservation held, payment not yet confirmed
 * CONFIRMED   - payment succeeded, seat locked
 * CANCELLED   - user or system cancelled
 * FAILED      - payment failed / timed out
 */
public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    FAILED
}
