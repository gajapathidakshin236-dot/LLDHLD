package com.company.booking.dto.response;

import com.company.booking.entity.Show;

import java.time.Instant;

public record ShowResponse(
    Long id,
    String title,
    String venue,
    Integer totalSeats,
    Long priceCents,
    Instant startsAt
) {
    public static ShowResponse from(Show s) {
        return new ShowResponse(s.getId(), s.getTitle(), s.getVenue(),
                                s.getTotalSeats(), s.getPriceCents(), s.getStartsAt());
    }
}
