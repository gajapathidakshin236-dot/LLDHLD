package com.company.parkinglot;

import java.time.LocalDateTime;

/**
 * STRATEGY #2: how to price a stay.
 *
 * IMPORTANT: calculateFee is PURE — it computes and returns a number.
 * It does NOT collect payment. Collecting money is a separate concern
 * (PaymentStrategy), so you can preview a bill without charging anyone.
 * Mixing the two would violate Single Responsibility.
 */
public interface FeeStrategy {
    double calculateFee(Ticket ticket, LocalDateTime exitTime);
}
