package com.flexjob.payment.domain.service;

import com.flexjob.payment.domain.enums.PaymentStatus;
import com.flexjob.payment.domain.model.Payment;
import com.flexjob.payment.domain.vo.Money;
import com.flexjob.payment.domain.vo.PaymentId;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDomainService {

    public Payment createPayment(Long bookingId, Long employeeId, Long employerId, BigDecimal amount) {
        if (bookingId == null || bookingId <= 0) {
            throw new IllegalArgumentException("Invalid booking ID");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        return Payment.builder()
                .id(null)
                .bookingId(bookingId)
                .employeeId(employeeId)
                .employerId(employerId)
                .status(PaymentStatus.PENDING)
                .amount(Money.euro(amount))
                .createdAt(LocalDateTime.now())
                .build();
    }
}
