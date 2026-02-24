package com.flexjob.booking.application.service;

import com.flexjob.booking.application.dto.response.BookingResponse;
import com.flexjob.booking.application.port.input.CompleteBookingUseCase;
import com.flexjob.booking.application.port.output.LoadBookingPort;
import com.flexjob.booking.application.port.output.PublishEventPort;
import com.flexjob.booking.application.port.output.SaveBookingPort;
import com.flexjob.booking.domain.exception.BookingNotFoundException;
import com.flexjob.booking.domain.model.Booking;
import com.flexjob.booking.domain.vo.BookingId;
import com.flexjob.booking.domain.vo.WorkingHours;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompleteBookingService implements CompleteBookingUseCase {

    private final LoadBookingPort loadBookingPort;
    private final SaveBookingPort saveBookingPort;
    private final PublishEventPort publishEventPort;

    @Override
    @Transactional
    public BookingResponse completeBooking(BookingId bookingId) {
        log.info("Completing booking: {}", bookingId.getValue());

        Booking booking = loadBookingPort.findById(bookingId)
                .orElseThrow(() -> BookingNotFoundException.byId(bookingId));

        // Complete via Domain method (validates business rules)
        Booking completed = booking.complete();

        Booking saved = saveBookingPort.save(completed);

        // Calculate total hours and publish event
        WorkingHours totalHours = saved.calculateTotalHours();
        publishEventPort.publishBookingCompleted(saved, totalHours);

        log.info("Booking completed, total hours: {}", totalHours.getHours());

        return mapToResponse(saved);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId().getValue())
                .jobId(booking.getJobId())
                .employeeId(booking.getEmployeeId())
                .employerId(booking.getEmployerId())
                .applicationId(booking.getApplicationId())
                .status(booking.getStatus())
                .startDate(booking.getDateRange().getStartDate())
                .endDate(booking.getDateRange().getEndDate())
                .totalHours(booking.calculateTotalHours().getHours())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
