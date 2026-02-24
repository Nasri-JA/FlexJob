package com.flexjob.booking.application.port.input;

import com.flexjob.booking.application.dto.command.EndTimeTrackingCommand;
import com.flexjob.booking.application.dto.response.TimeTrackingResponse;

public interface EndTimeTrackingUseCase {
    TimeTrackingResponse endTimeTracking(EndTimeTrackingCommand command);
}
