package com.flexjob.booking.application.service;

import com.flexjob.booking.application.dto.command.StartTimeTrackingCommand;
import com.flexjob.booking.application.dto.response.TimeTrackingResponse;
import com.flexjob.booking.application.port.input.StartTimeTrackingUseCase;
import com.flexjob.booking.application.port.output.LoadBookingPort;
import com.flexjob.booking.application.port.output.SaveBookingPort;
import com.flexjob.booking.application.port.output.SaveTimeTrackingPort;
import com.flexjob.booking.domain.enums.BookingStatus;
import com.flexjob.booking.domain.exception.BookingNotFoundException;
import com.flexjob.booking.domain.model.Booking;
import com.flexjob.booking.domain.model.TimeTracking;
import com.flexjob.booking.domain.service.BookingDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartTimeTrackingService implements StartTimeTrackingUseCase {

    private final BookingDomainService bookingDomainService;
    private final LoadBookingPort loadBookingPort;
    private final SaveBookingPort saveBookingPort;
    private final SaveTimeTrackingPort saveTimeTrackingPort;

    @Override
    @Transactional
    public TimeTrackingResponse startTimeTracking(StartTimeTrackingCommand command) {
        log.info("Starting time tracking for booking: {}", command.getBookingId().getValue());

        Booking booking = loadBookingPort.findById(command.getBookingId())
                .orElseThrow(() -> BookingNotFoundException.byId(command.getBookingId()));

        // Validate can start (no active tracking)
        booking.validateCanStartTimeTracking();

        // Create TimeTracking
        TimeTracking timeTracking = bookingDomainService.createTimeTracking(
                booking.getId().getValue(),
                LocalDateTime.now(),
                command.getNotes()
        );

        TimeTracking saved = saveTimeTrackingPort.save(timeTracking);

        // Update booking status to IN_PROGRESS if SCHEDULED
        if (booking.getStatus() == BookingStatus.SCHEDULED) {
            Booking updated = booking.updateStatus(BookingStatus.IN_PROGRESS);
            saveBookingPort.save(updated);
        }

        log.info("Time tracking started with ID: {}", saved.getId().getValue());

        return mapToResponse(saved);
    }

    private TimeTrackingResponse mapToResponse(TimeTracking timeTracking) {
        return TimeTrackingResponse.builder()
                .id(timeTracking.getId().getValue())
                .bookingId(timeTracking.getBookingId())
                .startTime(timeTracking.getPeriod().getStartTime())
                .endTime(timeTracking.getPeriod().getEndTime())
                .notes(timeTracking.getNotes())
                .hours(timeTracking.isCompleted() ? timeTracking.getHours() : null)
                .build();
    }
}
