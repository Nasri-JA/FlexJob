package com.flexjob.booking.domain.model;

import com.flexjob.booking.domain.vo.TimeTrackingId;
import com.flexjob.booking.domain.vo.TimeTrackingPeriod;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TimeTracking {

    private final TimeTrackingId id;
    private final Long bookingId;
    private TimeTrackingPeriod period;
    private String notes;

    public TimeTracking end(LocalDateTime endTime, String additionalNotes) {
        if (period.isCompleted()) {
            throw new IllegalStateException("Time tracking already ended");
        }

        String updatedNotes = this.notes;
        if (additionalNotes != null && !additionalNotes.trim().isEmpty()) {
            updatedNotes = this.notes != null
                    ? this.notes + "\n" + additionalNotes
                    : additionalNotes;
        }

        return TimeTracking.builder()
                .id(this.id)
                .bookingId(this.bookingId)
                .period(this.period.end(endTime))
                .notes(updatedNotes)
                .build();
    }

    public boolean isActive() {
        return period.isActive();
    }

    public boolean isCompleted() {
        return period.isCompleted();
    }

    public double getHours() {
        return period.getHours();
    }
}
