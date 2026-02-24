package com.flexjob.booking.application.port.output;

import com.flexjob.booking.domain.model.Booking;
import com.flexjob.booking.domain.vo.WorkingHours;

public interface PublishEventPort {
    void publishBookingCompleted(Booking booking, WorkingHours totalHours);
}
