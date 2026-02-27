package com.flexjob.engagement.booking.domain.exception;

import com.flexjob.engagement.booking.domain.vo.TimeTrackingId;

public class TimeTrackingNotFoundException extends RuntimeException {
    public TimeTrackingNotFoundException(String message) { super(message); }

    public static TimeTrackingNotFoundException byId(TimeTrackingId id) {
        return new TimeTrackingNotFoundException(String.format("TimeTracking not found with ID: %d", id.getValue()));
    }

    public static TimeTrackingNotFoundException byId(Long id) {
        return new TimeTrackingNotFoundException(String.format("TimeTracking not found with ID: %d", id));
    }

    public static TimeTrackingNotFoundException noActiveTracking(Long bookingId) {
        return new TimeTrackingNotFoundException(String.format("No active time tracking found for booking ID: %d", bookingId));
    }
}
