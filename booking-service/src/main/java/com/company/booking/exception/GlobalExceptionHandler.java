package com.company.booking.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

/**
 * Central exception mapping. Every controller in this app gets consistent
 * error responses without try/catch in the controllers themselves.
 * <p>
 * Order matters loosely - Spring picks the handler whose parameter type is the
 * closest match to the thrown exception, so keep {@code Exception.class} last.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // -------------------------------------------------------------------------
    // 400 - Bean Validation failures (@Valid on request bodies)
    // -------------------------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest req) {
        List<Map<String, String>> fields = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> Map.of("field", fe.getField(),
                              "message", fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage()))
            .toList();

        return ResponseEntity.badRequest().body(
            ErrorResponse.validation(400, "Request validation failed",
                                     req.getRequestURI(), fields));
    }

    // -------------------------------------------------------------------------
    // 400 - illegal argument / bad cursor
    // -------------------------------------------------------------------------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegal(IllegalArgumentException ex,
                                                       HttpServletRequest req) {
        return ResponseEntity.badRequest().body(
            ErrorResponse.of(400, "BadRequest", ex.getMessage(), req.getRequestURI()));
    }

    // -------------------------------------------------------------------------
    // AUDIT FIX #4: four client errors that previously fell through to the 500
    // catch-all. A 500 tells the client "our bug"; these are all "your input" -
    // they must be 400s. Each one maps to a concrete request you can try:
    // -------------------------------------------------------------------------

    /** ?size=0 on /feed - @Min/@Max on @RequestParam throws this (NOT
     *  MethodArgumentNotValidException, which is only for @Valid bodies). */
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleParamViolation(
            jakarta.validation.ConstraintViolationException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(
            ErrorResponse.of(400, "ConstraintViolation", ex.getMessage(), req.getRequestURI()));
    }

    /** GET /shows/abc - "abc" can't convert to Long. Also bad enum values. */
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex,
            HttpServletRequest req) {
        return ResponseEntity.badRequest().body(
            ErrorResponse.of(400, "TypeMismatch",
                "Parameter '" + ex.getName() + "' has invalid value '" + ex.getValue() + "'",
                req.getRequestURI()));
    }

    /** Malformed JSON body - Jackson fails before validation even runs. */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpServletRequest req) {
        return ResponseEntity.badRequest().body(
            ErrorResponse.of(400, "MalformedBody", "Request body is not valid JSON",
                req.getRequestURI()));
    }

    /** ?sort=notAField - Spring Data rejects unknown sort properties. */
    @ExceptionHandler(org.springframework.data.mapping.PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handleBadSort(
            org.springframework.data.mapping.PropertyReferenceException ex,
            HttpServletRequest req) {
        return ResponseEntity.badRequest().body(
            ErrorResponse.of(400, "InvalidSort", ex.getMessage(), req.getRequestURI()));
    }

    // -------------------------------------------------------------------------
    // 409 - optimistic lock: two concurrent edits, the slower one loses.
    // Client should re-fetch and retry. (See Show.java @Version.)
    // -------------------------------------------------------------------------
    @ExceptionHandler(org.springframework.orm.ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(
            org.springframework.orm.ObjectOptimisticLockingFailureException ex,
            HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse.of(409, "ConcurrentModification",
                "Resource was modified by another request - refetch and retry",
                req.getRequestURI()));
    }

    // -------------------------------------------------------------------------
    // 404 - resource missing
    // -------------------------------------------------------------------------
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(BookingNotFoundException ex,
                                                        HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse.of(404, "NotFound", ex.getMessage(), req.getRequestURI()));
    }

    // -------------------------------------------------------------------------
    // 409 - seat conflict
    // -------------------------------------------------------------------------
    @ExceptionHandler(SeatUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleConflict(SeatUnavailableException ex,
                                                        HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse.of(409, "Conflict", ex.getMessage(), req.getRequestURI()));
    }

    // -------------------------------------------------------------------------
    // 500 - catch-all. Log the stack; never expose it to the client.
    // -------------------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception on {}: ", req.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse.of(500, "InternalError", "Something went wrong", req.getRequestURI()));
    }
}
