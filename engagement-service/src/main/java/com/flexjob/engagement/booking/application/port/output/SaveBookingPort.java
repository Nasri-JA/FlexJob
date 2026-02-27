package com.flexjob.engagement.booking.application.port.output;

import com.flexjob.engagement.booking.domain.model.Booking;

public interface SaveBookingPort {
    Booking save(Booking booking);
}
