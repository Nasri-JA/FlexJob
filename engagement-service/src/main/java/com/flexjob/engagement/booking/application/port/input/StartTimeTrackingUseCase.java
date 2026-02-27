package com.flexjob.engagement.booking.application.port.input;

import com.flexjob.engagement.booking.application.dto.command.StartTimeTrackingCommand;
import com.flexjob.engagement.booking.application.dto.response.TimeTrackingResponse;

public interface StartTimeTrackingUseCase {
    TimeTrackingResponse startTimeTracking(StartTimeTrackingCommand command);
}
