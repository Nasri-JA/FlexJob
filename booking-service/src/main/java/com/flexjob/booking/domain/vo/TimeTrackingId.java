package com.flexjob.booking.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TimeTrackingId {

    private final Long value;

    private TimeTrackingId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("TimeTracking ID must be positive, got: " + value);
        }
        this.value = value;
    }

    public static TimeTrackingId of(Long value) {
        return new TimeTrackingId(value);
    }

    @Override
    public String toString() {
        return "TimeTrackingId(" + value + ")";
    }
}
