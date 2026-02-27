package com.flexjob.engagement.booking.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BookingId {
    private final Long value;

    private BookingId(Long value) {
        if (value == null || value <= 0) throw new IllegalArgumentException("Booking ID must be positive, got: " + value);
        this.value = value;
    }

    public static BookingId of(Long value) { return new BookingId(value); }

    @Override
    public String toString() { return "BookingId(" + value + ")"; }
}
