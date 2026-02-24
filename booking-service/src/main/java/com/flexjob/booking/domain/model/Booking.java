package com.flexjob.booking.domain.model;

import com.flexjob.booking.domain.enums.BookingStatus;
import com.flexjob.booking.domain.vo.BookingId;
import com.flexjob.booking.domain.vo.DateRange;
import com.flexjob.booking.domain.vo.WorkingHours;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class Booking {

    private final BookingId id;
    private final Long jobId;
    private final Long employeeId;
    private final Long employerId;
    private final Long applicationId;
    private BookingStatus status;
    private final DateRange dateRange;
    private final LocalDateTime createdAt;

    @Builder.Default
    private final List<TimeTracking> timeTrackings = new ArrayList<>();

    public Booking updateStatus(BookingStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s", status, newStatus)
            );
        }

        if (status == newStatus) {
            throw new IllegalStateException("Booking already has status: " + newStatus);
        }

        return Booking.builder()
                .id(this.id)
                .jobId(this.jobId)
                .employeeId(this.employeeId)
                .employerId(this.employerId)
                .applicationId(this.applicationId)
                .status(newStatus)
                .dateRange(this.dateRange)
                .createdAt(this.createdAt)
                .timeTrackings(this.timeTrackings)
                .build();
    }

    public void validateCanStartTimeTracking() {
        boolean hasActiveTracking = timeTrackings.stream()
                .anyMatch(TimeTracking::isActive);

        if (hasActiveTracking) {
            throw new IllegalStateException("Time tracking already in progress for this booking");
        }

        if (status == BookingStatus.COMPLETED || status == BookingStatus.CANCELLED) {
            throw new IllegalStateException(
                    String.format("Cannot start time tracking for booking with status %s", status)
            );
        }
    }

    public Booking complete() {
        boolean hasActiveTracking = timeTrackings.stream()
                .anyMatch(TimeTracking::isActive);

        if (hasActiveTracking) {
            throw new IllegalStateException(
                    "Cannot complete booking with active time tracking. Please end all time tracking first."
            );
        }

        return updateStatus(BookingStatus.COMPLETED);
    }

    public Booking cancel() {
        return updateStatus(BookingStatus.CANCELLED);
    }

    public WorkingHours calculateTotalHours() {
        double totalHours = timeTrackings.stream()
                .filter(TimeTracking::isCompleted)
                .mapToDouble(TimeTracking::getHours)
                .sum();

        return WorkingHours.of(totalHours);
    }

    public List<TimeTracking> getTimeTrackings() {
        return Collections.unmodifiableList(timeTrackings);
    }

    public void addTimeTracking(TimeTracking timeTracking) {
        this.timeTrackings.add(timeTracking);
    }

    public boolean isCompleted() {
        return status == BookingStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return status == BookingStatus.CANCELLED;
    }

    public boolean isFinal() {
        return status.isFinal();
    }
}
