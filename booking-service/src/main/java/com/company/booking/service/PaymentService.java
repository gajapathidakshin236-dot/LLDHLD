package com.company.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Fake payment provider. Simulates a call to Stripe/Razorpay.
 *
 * AUDIT FIX #3 - this used to decline randomly (10% of calls). Random behavior
 * makes TESTS FLAKY: the create-booking test could not assert an exact status.
 * Rule of thumb: never put nondeterminism inside a boundary you want to test.
 * Now the outcome is a documented deterministic rule:
 *
 *      userId ending in 0  ->  DECLINED      (e.g. 9010)
 *      anything else       ->  SUCCESS       (e.g. 9001)
 *
 * Same lesson at real scale: inject a Clock instead of calling Instant.now(),
 * seed your Randoms, and fake external providers with controllable rules.
 *
 * Deliberately NOT annotated @Transactional. Calls to external systems have
 * side effects that can't be rolled back - keep them OUT of DB transactions.
 */
@Slf4j
@Service
public class PaymentService {

    public record PaymentResult(boolean success, String reference, String message) {}

    public PaymentResult charge(Long userId, Long priceCents) {
        // Simulate network latency of a real provider call
        try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        boolean declined = userId % 10 == 0;      // deterministic rule (see class Javadoc)
        String ref = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        if (!declined) {
            log.info("Payment success: user={} amount={} ref={}", userId, priceCents, ref);
            return new PaymentResult(true, ref, "OK");
        }
        log.warn("Payment declined: user={} amount={}", userId, priceCents);
        return new PaymentResult(false, null, "Card declined");
    }
}
