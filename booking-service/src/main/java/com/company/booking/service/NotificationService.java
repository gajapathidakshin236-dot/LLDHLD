package com.company.booking.service;

import com.company.booking.event.BookingConfirmedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Third service in the collaboration - reacts to booking confirmations.
 *
 * TWO annotations doing precise jobs:
 *
 * @TransactionalEventListener(phase = AFTER_COMMIT)
 *   A plain @EventListener fires the moment publishEvent() is called - INSIDE
 *   the transaction. If the tx then rolls back, you've sent an email for a
 *   booking that never happened. AFTER_COMMIT defers the listener until the
 *   commit actually succeeded. If the tx rolls back, the listener never runs.
 *
 * @Async("notificationExecutor")
 *   Runs the listener on a thread-pool thread instead of the request thread.
 *   The HTTP response returns immediately; the "email" is sent in the background.
 *   Requires @EnableAsync (see AsyncConfig). Failure here does NOT fail the
 *   booking - notifications are best-effort by design.
 */
@Slf4j
@Service
public class NotificationService {

    @Async("notificationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingConfirmed(BookingConfirmedEvent event) {
        // Simulate an email/SMS/push provider call
        log.info("[notification] (async, post-commit) booking={} tenant={} user={} seat={} - sending confirmation",
            event.bookingId(), event.tenantId(), event.userId(), event.seatNo());

        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        log.info("[notification] sent for booking={}", event.bookingId());
    }
}
