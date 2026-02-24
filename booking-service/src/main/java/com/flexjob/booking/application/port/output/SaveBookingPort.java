package com.flexjob.booking.application.port.output;

import com.flexjob.booking.domain.model.Booking;

public interface SaveBookingPort {
    Booking save(Booking booking);
}
