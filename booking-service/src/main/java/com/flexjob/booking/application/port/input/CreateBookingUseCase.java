package com.flexjob.booking.application.port.input;

import com.flexjob.booking.application.dto.command.CreateBookingCommand;
import com.flexjob.booking.application.dto.response.BookingResponse;

public interface CreateBookingUseCase {
    BookingResponse createBooking(CreateBookingCommand command);
}
