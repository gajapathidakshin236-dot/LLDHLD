package com.company.booking.dto.response;

import com.company.booking.entity.Booking;
import com.company.booking.entity.BookingStatus;

import java.time.Instant;

/**
 * DTO returned by the API - never leak the entity.
 * <p>
 * Reasons to have a separate response type:
 *   - stability: entity fields can change without breaking clients
 *   - security: no accidental exposure of internal columns
 *   - shape: you can compose/rename fields without JPA getting in the way
 */
public record BookingResponse(
    Long id,
    Long showId,
    Long userId,
    String seatNo,
    BookingStatus status,
    Long priceCents,
    Instant createdAt
) {
    public static BookingResponse from(Booking b) {
        return new BookingResponse(
            b.getId(), b.getShowId(), b.getUserId(), b.getSeatNo(),
            b.getStatus(), b.getPriceCents(), b.getCreatedAt()
        );
    }
}
