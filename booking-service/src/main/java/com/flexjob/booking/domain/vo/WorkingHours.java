package com.flexjob.booking.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class WorkingHours {

    private final double hours;

    private WorkingHours(double hours) {
        this.hours = validateHours(hours);
    }

    public static WorkingHours of(double hours) {
        return new WorkingHours(hours);
    }

    public static WorkingHours zero() {
        return new WorkingHours(0.0);
    }

    private double validateHours(double hours) {
        if (hours < 0) {
            throw new IllegalArgumentException(
                    String.format("Working hours cannot be negative, got: %.2f", hours)
            );
        }
        return hours;
    }

    public WorkingHours add(WorkingHours other) {
        return new WorkingHours(this.hours + other.hours);
    }

    public WorkingHours add(double additionalHours) {
        return new WorkingHours(this.hours + additionalHours);
    }

    public WorkingHours multiply(double factor) {
        return new WorkingHours(this.hours * factor);
    }

    public boolean hasHours() {
        return hours > 0;
    }

    public boolean isEmpty() {
        return hours == 0.0;
    }

    @Override
    public String toString() {
        return String.format("WorkingHours(%.2f hours)", hours);
    }
}
