package com.flexjob.engagement.booking.application.service;

import com.flexjob.engagement.booking.application.dto.command.EndTimeTrackingCommand;
import com.flexjob.engagement.booking.application.dto.response.TimeTrackingResponse;
import com.flexjob.engagement.booking.application.port.input.EndTimeTrackingUseCase;
import com.flexjob.engagement.booking.application.port.output.LoadTimeTrackingPort;
import com.flexjob.engagement.booking.application.port.output.SaveTimeTrackingPort;
import com.flexjob.engagement.booking.domain.exception.TimeTrackingNotFoundException;
import com.flexjob.engagement.booking.domain.model.TimeTracking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EndTimeTrackingService implements EndTimeTrackingUseCase {

    private final LoadTimeTrackingPort loadTimeTrackingPort;
    private final SaveTimeTrackingPort saveTimeTrackingPort;

    @Override
    @Transactional
    public TimeTrackingResponse endTimeTracking(EndTimeTrackingCommand command) {
        log.info("Ending time tracking for booking: {}", command.getBookingId().getValue());
        TimeTracking timeTracking = loadTimeTrackingPort.findActiveByBookingId(command.getBookingId().getValue())
                .orElseThrow(() -> TimeTrackingNotFoundException.noActiveTracking(command.getBookingId().getValue()));
        TimeTracking ended = timeTracking.end(LocalDateTime.now(), command.getNotes());
        TimeTracking saved = saveTimeTrackingPort.save(ended);
        log.info("Time tracking ended, hours: {}", saved.getHours());
        return mapToResponse(saved);
    }

    private TimeTrackingResponse mapToResponse(TimeTracking timeTracking) {
        return TimeTrackingResponse.builder().id(timeTracking.getId().getValue())
                .bookingId(timeTracking.getBookingId()).startTime(timeTracking.getPeriod().getStartTime())
                .endTime(timeTracking.getPeriod().getEndTime()).notes(timeTracking.getNotes())
                .hours(timeTracking.getHours()).build();
    }
}
