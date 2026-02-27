package com.flexjob.engagement.booking.application.port.input;

import com.flexjob.engagement.booking.application.dto.command.CreateBookingCommand;
import com.flexjob.engagement.booking.application.dto.response.BookingResponse;

public interface CreateBookingUseCase {
    BookingResponse createBooking(CreateBookingCommand command);
}
