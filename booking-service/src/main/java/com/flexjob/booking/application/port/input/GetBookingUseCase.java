package com.flexjob.booking.application.port.input;

import com.flexjob.booking.application.dto.response.BookingResponse;
import com.flexjob.booking.domain.vo.BookingId;
import java.util.List;

public interface GetBookingUseCase {
    BookingResponse getBookingById(BookingId id);
    List<BookingResponse> getBookingsByEmployeeId(Long employeeId);
    List<BookingResponse> getBookingsByEmployerId(Long employerId);
    List<BookingResponse> getBookingsByJobId(Long jobId);
}
