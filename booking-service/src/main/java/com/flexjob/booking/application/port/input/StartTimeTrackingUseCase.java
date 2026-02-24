package com.flexjob.booking.application.port.input;

import com.flexjob.booking.application.dto.command.StartTimeTrackingCommand;
import com.flexjob.booking.application.dto.response.TimeTrackingResponse;

public interface StartTimeTrackingUseCase {
    TimeTrackingResponse startTimeTracking(StartTimeTrackingCommand command);
}
