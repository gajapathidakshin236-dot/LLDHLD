package com.company.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Record + Bean Validation annotations.
 * <p>
 * Controller uses {@code @Valid} to trigger these; failures become 400s via
 * {@link com.company.booking.exception.GlobalExceptionHandler#handleValidation}.
 */
public record CreateBookingRequest(
    @NotNull  @Positive Long showId,
    @NotNull  @Positive Long userId,
    @NotBlank @Size(max = 10) String seatNo
) {}
