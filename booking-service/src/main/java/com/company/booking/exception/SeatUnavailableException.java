package com.company.booking.exception;

/** 409 - seat is already booked (either detected at app level or by UNIQUE constraint). */
public class SeatUnavailableException extends RuntimeException {
    public SeatUnavailableException(String message) { super(message); }
}
