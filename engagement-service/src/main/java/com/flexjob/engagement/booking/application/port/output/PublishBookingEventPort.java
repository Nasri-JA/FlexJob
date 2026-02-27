package com.flexjob.engagement.booking.application.port.output;

import com.flexjob.engagement.booking.domain.model.Booking;
import com.flexjob.engagement.booking.domain.vo.WorkingHours;

public interface PublishBookingEventPort {
    void publishBookingCompleted(Booking booking, WorkingHours totalHours);
}
