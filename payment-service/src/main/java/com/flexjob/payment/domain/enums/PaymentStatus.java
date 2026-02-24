package com.flexjob.payment.domain.enums;

public enum PaymentStatus {
    PENDING, COMPLETED, FAILED, REFUNDED;

    public boolean isFinal() {
        return this == COMPLETED || this == REFUNDED;
    }
}
