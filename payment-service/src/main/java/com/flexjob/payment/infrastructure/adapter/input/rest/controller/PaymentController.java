package com.flexjob.payment.infrastructure.adapter.input.rest.controller;

import com.flexjob.payment.application.dto.response.PaymentResponse;
import com.flexjob.payment.application.port.input.*;
import com.flexjob.payment.domain.vo.PaymentId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final GetPaymentUseCase getPaymentUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getPaymentUseCase.getPaymentById(PaymentId.of(id)));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentResponse>> getByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(getPaymentUseCase.getPaymentsByBookingId(bookingId));
    }
}
