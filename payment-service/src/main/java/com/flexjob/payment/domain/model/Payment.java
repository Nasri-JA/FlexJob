package com.flexjob.payment.domain.model;

import com.flexjob.payment.domain.enums.PaymentStatus;
import com.flexjob.payment.domain.vo.Money;
import com.flexjob.payment.domain.vo.PaymentId;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Payment {
    private final PaymentId id;
    private final Long bookingId;
    private final Long employeeId;
    private final Long employerId;
    private PaymentStatus status;
    private final Money amount;
    private final LocalDateTime createdAt;
    @Builder.Default
    private final List<Transaction> transactions = new ArrayList<>();

    public Payment processPayment() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Can only process PENDING payments");
        }
        return Payment.builder()
                .id(this.id).bookingId(this.bookingId)
                .employeeId(this.employeeId).employerId(this.employerId)
                .status(PaymentStatus.COMPLETED)
                .amount(this.amount).createdAt(this.createdAt)
                .transactions(this.transactions)
                .build();
    }

    public Payment markAsFailed() {
        return Payment.builder()
                .id(this.id).bookingId(this.bookingId)
                .employeeId(this.employeeId).employerId(this.employerId)
                .status(PaymentStatus.FAILED)
                .amount(this.amount).createdAt(this.createdAt)
                .transactions(this.transactions)
                .build();
    }
}
