package com.flexjob.engagement.booking.domain.exception;

import com.flexjob.engagement.booking.domain.vo.BookingId;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) { super(message); }

    public static BookingNotFoundException byId(BookingId id) {
        return new BookingNotFoundException(String.format("Booking not found with ID: %d", id.getValue()));
    }

    public static BookingNotFoundException byId(Long id) {
        return new BookingNotFoundException(String.format("Booking not found with ID: %d", id));
    }
}
