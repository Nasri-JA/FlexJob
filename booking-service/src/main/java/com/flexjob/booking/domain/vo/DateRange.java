package com.flexjob.booking.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@EqualsAndHashCode
public class DateRange {

    private final LocalDate startDate;
    private final LocalDate endDate;

    private DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = validateStartDate(startDate);
        this.endDate = validateEndDate(startDate, endDate);
    }

    public static DateRange of(LocalDate startDate, LocalDate endDate) {
        return new DateRange(startDate, endDate);
    }

    private LocalDate validateStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        return startDate;
    }

    private LocalDate validateEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    String.format("End date %s must be after or equal to start date %s",
                            endDate, startDate)
            );
        }

        return endDate;
    }

    public long getDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean isInPast() {
        return endDate.isBefore(LocalDate.now());
    }

    public boolean isInFuture() {
        return startDate.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        return String.format("DateRange(%s to %s, %d days)",
                startDate, endDate, getDurationInDays());
    }
}
