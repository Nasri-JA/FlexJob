package com.flexjob.engagement.booking.application.port.output;

import com.flexjob.engagement.booking.domain.model.TimeTracking;
import com.flexjob.engagement.booking.domain.vo.TimeTrackingId;
import java.util.List;
import java.util.Optional;

public interface LoadTimeTrackingPort {
    Optional<TimeTracking> findById(TimeTrackingId id);
    List<TimeTracking> findByBookingId(Long bookingId);
    Optional<TimeTracking> findActiveByBookingId(Long bookingId);
}
