package com.flexjob.engagement.booking.application.port.input;

import com.flexjob.engagement.booking.application.dto.command.EndTimeTrackingCommand;
import com.flexjob.engagement.booking.application.dto.response.TimeTrackingResponse;

public interface EndTimeTrackingUseCase {
    TimeTrackingResponse endTimeTracking(EndTimeTrackingCommand command);
}
