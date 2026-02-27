package com.flexjob.engagement.booking.application.port.output;

import com.flexjob.engagement.booking.domain.model.TimeTracking;

public interface SaveTimeTrackingPort {
    TimeTracking save(TimeTracking timeTracking);
}
