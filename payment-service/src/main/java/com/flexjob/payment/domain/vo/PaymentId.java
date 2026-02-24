package com.flexjob.payment.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PaymentId {
    private final Long value;

    private PaymentId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Payment ID must be positive");
        }
        this.value = value;
    }

    public static PaymentId of(Long value) {
        return new PaymentId(value);
    }

    @Override
    public String toString() {
        return "PaymentId(" + value + ")";
    }
}
