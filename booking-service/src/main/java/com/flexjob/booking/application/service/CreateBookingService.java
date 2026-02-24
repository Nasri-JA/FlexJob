package com.flexjob.booking.application.service;

import com.flexjob.booking.application.dto.command.CreateBookingCommand;
import com.flexjob.booking.application.dto.response.BookingResponse;
import com.flexjob.booking.application.port.input.CreateBookingUseCase;
import com.flexjob.booking.application.port.output.LoadBookingPort;
import com.flexjob.booking.application.port.output.SaveBookingPort;
import com.flexjob.booking.domain.model.Booking;
import com.flexjob.booking.domain.service.BookingDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateBookingService implements CreateBookingUseCase {

    private final BookingDomainService bookingDomainService;
    private final LoadBookingPort loadBookingPort;
    private final SaveBookingPort saveBookingPort;

    @Override
    @Transactional
    public BookingResponse createBooking(CreateBookingCommand command) {
        log.info("Creating booking for job: {}, employee: {}", command.getJobId(), command.getEmployeeId());

        // Check for duplicate
        if (loadBookingPort.existsByApplicationId(command.getApplicationId())) {
            throw new IllegalStateException("Booking already exists for this application");
        }

        // Create via Domain Service
        Booking booking = bookingDomainService.createBooking(
                command.getJobId(),
                command.getEmployeeId(),
                command.getEmployerId(),
                command.getApplicationId(),
                command.getStartDate(),
                command.getEndDate()
        );

        Booking saved = saveBookingPort.save(booking);
        log.info("Booking created with ID: {}", saved.getId().getValue());

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
