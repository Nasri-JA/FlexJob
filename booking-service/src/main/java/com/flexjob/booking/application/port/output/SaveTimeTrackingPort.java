package com.flexjob.booking.application.port.output;

import com.flexjob.booking.domain.model.TimeTracking;

public interface SaveTimeTrackingPort {
    TimeTracking save(TimeTracking timeTracking);
}
