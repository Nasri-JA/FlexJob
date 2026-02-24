package com.flexjob.booking.domain.service;

import com.flexjob.booking.domain.enums.BookingStatus;
import com.flexjob.booking.domain.model.Booking;
import com.flexjob.booking.domain.model.TimeTracking;
import com.flexjob.booking.domain.vo.DateRange;
import com.flexjob.booking.domain.vo.TimeTrackingPeriod;
import com.flexjob.booking.domain.vo.WorkingHours;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookingDomainService {

    public Booking createBooking(
            Long jobId,
            Long employeeId,
            Long employerId,
            Long applicationId,
            LocalDate startDate,
            LocalDate endDate) {

        validateId(jobId, "Job ID");
        validateId(employeeId, "Employee ID");
        validateId(employerId, "Employer ID");
        validateId(applicationId, "Application ID");

        DateRange dateRange = DateRange.of(startDate, endDate);

        return Booking.builder()
                .id(null)
                .jobId(jobId)
                .employeeId(employeeId)
                .employerId(employerId)
                .applicationId(applicationId)
                .status(BookingStatus.SCHEDULED)
                .dateRange(dateRange)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public TimeTracking createTimeTracking(
            Long bookingId,
            LocalDateTime startTime,
            String notes) {

        validateId(bookingId, "Booking ID");

        if (notes != null && notes.length() > 1000) {
            throw new IllegalArgumentException(
                    String.format("Notes cannot exceed 1000 characters, got: %d", notes.length())
            );
        }

        TimeTrackingPeriod period = TimeTrackingPeriod.started(startTime);

        return TimeTracking.builder()
                .id(null)
                .bookingId(bookingId)
                .period(period)
                .notes(notes)
                .build();
    }

    public WorkingHours calculateTotalHours(List<TimeTracking> timeTrackings) {
        if (timeTrackings == null || timeTrackings.isEmpty()) {
            return WorkingHours.zero();
        }

        double totalHours = timeTrackings.stream()
                .filter(TimeTracking::isCompleted)
                .mapToDouble(TimeTracking::getHours)
                .sum();

        return WorkingHours.of(totalHours);
    }

    public void validateStatusUpdateTransition(BookingStatus currentStatus, BookingStatus newStatus) {
        if (currentStatus.isFinal()) {
            throw new IllegalStateException(
                    String.format("Cannot change status of a %s booking", currentStatus)
            );
        }

        if (newStatus == BookingStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Use completeBooking endpoint to complete a booking"
            );
        }
    }

    public boolean canModifyBooking(Booking booking, Long userId) {
        return booking.getEmployeeId().equals(userId) || booking.getEmployerId().equals(userId);
    }

    private void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    String.format("%s must be positive, got: %s", fieldName, id)
            );
        }
    }
}
