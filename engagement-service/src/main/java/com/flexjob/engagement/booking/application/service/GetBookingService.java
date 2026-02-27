package com.flexjob.engagement.booking.application.service;

import com.flexjob.engagement.booking.application.dto.response.BookingResponse;
import com.flexjob.engagement.booking.application.port.input.GetBookingUseCase;
import com.flexjob.engagement.booking.application.port.output.LoadBookingPort;
import com.flexjob.engagement.booking.domain.exception.BookingNotFoundException;
import com.flexjob.engagement.booking.domain.model.Booking;
import com.flexjob.engagement.booking.domain.vo.BookingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetBookingService implements GetBookingUseCase {

    private final LoadBookingPort loadBookingPort;

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(BookingId id) {
        Booking booking = loadBookingPort.findById(id).orElseThrow(() -> BookingNotFoundException.byId(id));
        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByEmployeeId(Long employeeId) {
        return loadBookingPort.findByEmployeeId(employeeId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByEmployerId(Long employerId) {
        return loadBookingPort.findByEmployerId(employerId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByJobId(Long jobId) {
        return loadBookingPort.findByJobId(jobId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder().id(booking.getId().getValue()).jobId(booking.getJobId())
                .employeeId(booking.getEmployeeId()).employerId(booking.getEmployerId())
                .applicationId(booking.getApplicationId()).status(booking.getStatus())
                .startDate(booking.getDateRange().getStartDate()).endDate(booking.getDateRange().getEndDate())
                .totalHours(booking.calculateTotalHours().getHours()).createdAt(booking.getCreatedAt()).build();
    }
}
