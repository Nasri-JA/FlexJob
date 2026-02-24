package com.flexjob.payment.infrastructure.adapter.output.persistence.mapper;

import com.flexjob.payment.domain.model.Payment;
import com.flexjob.payment.domain.vo.Money;
import com.flexjob.payment.domain.vo.PaymentId;
import com.flexjob.payment.infrastructure.adapter.output.persistence.entity.PaymentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceMapper {

    public PaymentJpaEntity toJpaEntity(Payment payment) {
        return PaymentJpaEntity.builder()
                .id(payment.getId() != null ? payment.getId().getValue() : null)
                .bookingId(payment.getBookingId())
                .employeeId(payment.getEmployeeId())
                .employerId(payment.getEmployerId())
                .status(payment.getStatus())
                .amount(payment.getAmount().getAmount())
                .currency(payment.getAmount().getCurrency())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public Payment toDomain(PaymentJpaEntity jpa) {
        return Payment.builder()
                .id(PaymentId.of(jpa.getId()))
                .bookingId(jpa.getBookingId())
                .employeeId(jpa.getEmployeeId())
                .employerId(jpa.getEmployerId())
                .status(jpa.getStatus())
                .amount(Money.of(jpa.getAmount(), jpa.getCurrency()))
                .createdAt(jpa.getCreatedAt())
                .build();
    }
}
