package com.flexjob.booking.application.port.input;

import com.flexjob.booking.application.dto.response.BookingResponse;
import com.flexjob.booking.domain.vo.BookingId;

public interface CompleteBookingUseCase {
    BookingResponse completeBooking(BookingId bookingId);
}
