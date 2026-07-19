package com.company.booking.exception;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Consistent error shape - clients see the same JSON structure for every error.
 * Modeled loosely on RFC 7807 (problem+json) but simplified.
 */
public record ErrorResponse(
    int status,
    String error,
    String message,
    String path,
    Instant timestamp,
    List<Map<String, String>> fieldErrors    // populated on validation failures
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, path, Instant.now(), List.of());
    }

    public static ErrorResponse validation(int status, String message, String path,
                                           List<Map<String, String>> fieldErrors) {
        return new ErrorResponse(status, "ValidationError", message, path,
                                 Instant.now(), fieldErrors);
    }
}
