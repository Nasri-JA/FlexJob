package com.flexjob.engagement.booking.application.port.input;

import com.flexjob.engagement.booking.application.dto.response.BookingResponse;
import com.flexjob.engagement.booking.domain.vo.BookingId;

public interface CompleteBookingUseCase {
    BookingResponse completeBooking(BookingId bookingId);
}
