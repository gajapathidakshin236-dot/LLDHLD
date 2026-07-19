package com.company.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;

/**
 * Body for PUT /api/v1/shows/{id}.
 *
 * PUT vs PATCH - the interview-grade distinction:
 *   PUT   = FULL replacement. The client sends EVERY field; anything you leave
 *           out is (conceptually) set to nothing. That's why every field here
 *           is @NotNull - a partial PUT body is a client error.
 *   PATCH = partial update. Only the fields present change (see
 *           PATCH /shows/{id}/title which touches one field).
 */
public record UpdateShowRequest(
    @NotBlank @Size(max = 200) String title,
    @NotBlank @Size(max = 200) String venue,
    @NotNull  @Positive Integer totalSeats,
    @NotNull  @Positive Long priceCents,
    @NotNull  Instant startsAt
) {}
