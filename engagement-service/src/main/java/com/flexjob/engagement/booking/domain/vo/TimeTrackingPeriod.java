package com.flexjob.engagement.booking.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class TimeTrackingPeriod {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private TimeTrackingPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = validateStartTime(startTime);
        this.endTime = endTime != null ? validateEndTime(startTime, endTime) : null;
    }

    public static TimeTrackingPeriod started(LocalDateTime startTime) { return new TimeTrackingPeriod(startTime, null); }
    public static TimeTrackingPeriod completed(LocalDateTime startTime, LocalDateTime endTime) { return new TimeTrackingPeriod(startTime, endTime); }

    private LocalDateTime validateStartTime(LocalDateTime startTime) {
        if (startTime == null) throw new IllegalArgumentException("Start time cannot be null");
        return startTime;
    }

    private LocalDateTime validateEndTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) throw new IllegalArgumentException(
                String.format("End time %s must be after start time %s", endTime, startTime));
        return endTime;
    }

    public TimeTrackingPeriod end(LocalDateTime endTime) {
        if (this.endTime != null) throw new IllegalStateException("Time tracking already ended");
        return completed(this.startTime, endTime);
    }

    public boolean isActive() { return endTime == null; }
    public boolean isCompleted() { return endTime != null; }

    public double getHours() {
        if (endTime == null) throw new IllegalStateException("Cannot calculate hours for active time tracking");
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes() / 60.0;
    }

    @Override
    public String toString() {
        if (endTime == null) return String.format("TimeTrackingPeriod(started at %s, ACTIVE)", startTime);
        return String.format("TimeTrackingPeriod(%s to %s, %.2f hours)", startTime, endTime, getHours());
    }
}
