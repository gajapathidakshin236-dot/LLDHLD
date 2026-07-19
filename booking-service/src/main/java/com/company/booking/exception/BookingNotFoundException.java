package com.company.booking.exception;

/** 404 - thrown by services when an id doesn't exist for the current tenant. */
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) { super(message); }
}
