package com.flexjob.payment.application.port.input;

import com.flexjob.payment.application.dto.response.PaymentResponse;
import com.flexjob.payment.domain.vo.PaymentId;
import java.util.List;

public interface GetPaymentUseCase {
    PaymentResponse getPaymentById(PaymentId id);
    List<PaymentResponse> getPaymentsByBookingId(Long bookingId);
}
