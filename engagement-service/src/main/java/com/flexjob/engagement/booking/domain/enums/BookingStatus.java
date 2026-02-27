package com.flexjob.engagement.booking.domain.enums;

public enum BookingStatus {
    SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED;

    public boolean isFinal() { return this == COMPLETED || this == CANCELLED; }

    public boolean canTransitionTo(BookingStatus newStatus) {
        if (this.isFinal()) return false;
        if (this == SCHEDULED) return true;
        if (this == IN_PROGRESS) return newStatus == COMPLETED || newStatus == CANCELLED;
        return false;
    }
}
