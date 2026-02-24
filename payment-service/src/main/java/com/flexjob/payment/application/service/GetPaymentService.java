package com.flexjob.payment.application.service;

import com.flexjob.payment.application.dto.response.PaymentResponse;
import com.flexjob.payment.application.port.input.GetPaymentUseCase;
import com.flexjob.payment.application.port.output.LoadPaymentPort;
import com.flexjob.payment.domain.exception.PaymentNotFoundException;
import com.flexjob.payment.domain.model.Payment;
import com.flexjob.payment.domain.vo.PaymentId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPaymentService implements GetPaymentUseCase {

    private final LoadPaymentPort loadPaymentPort;

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(PaymentId id) {
        Payment payment = loadPaymentPort.findById(id)
                .orElseThrow(() -> PaymentNotFoundException.byId(id.getValue()));
        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByBookingId(Long bookingId) {
        return loadPaymentPort.findByBookingId(bookingId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId().getValue())
                .bookingId(payment.getBookingId())
                .employeeId(payment.getEmployeeId())
                .employerId(payment.getEmployerId())
                .status(payment.getStatus())
                .amount(payment.getAmount().getAmount())
                .currency(payment.getAmount().getCurrency())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
