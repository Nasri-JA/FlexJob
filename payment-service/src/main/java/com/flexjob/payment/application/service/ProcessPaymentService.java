package com.flexjob.payment.application.service;

import com.flexjob.payment.application.dto.command.ProcessPaymentCommand;
import com.flexjob.payment.application.dto.response.PaymentResponse;
import com.flexjob.payment.application.port.input.ProcessPaymentUseCase;
import com.flexjob.payment.application.port.output.*;
import com.flexjob.payment.domain.model.Payment;
import com.flexjob.payment.domain.service.PaymentDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessPaymentService implements ProcessPaymentUseCase {

    private final PaymentDomainService paymentDomainService;
    private final LoadPaymentPort loadPaymentPort;
    private final SavePaymentPort savePaymentPort;
    private final PublishEventPort publishEventPort;

    @Override
    @Transactional
    public PaymentResponse processPayment(ProcessPaymentCommand command) {
        log.info("Processing payment for booking: {}", command.getBookingId());

        if (loadPaymentPort.existsByBookingId(command.getBookingId())) {
            throw new IllegalStateException("Payment already exists for this booking");
        }

        Payment payment = paymentDomainService.createPayment(
                command.getBookingId(),
                command.getEmployeeId(),
                command.getEmployerId(),
                command.getAmount()
        );

        Payment processed = payment.processPayment();
        Payment saved = savePaymentPort.save(processed);

        publishEventPort.publishPaymentCompleted(saved);

        log.info("Payment processed with ID: {}", saved.getId().getValue());

        return PaymentResponse.builder()
                .id(saved.getId().getValue())
                .bookingId(saved.getBookingId())
                .employeeId(saved.getEmployeeId())
                .employerId(saved.getEmployerId())
                .status(saved.getStatus())
                .amount(saved.getAmount().getAmount())
                .currency(saved.getAmount().getCurrency())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
