package com.flexjob.payment.application.port.output;

import com.flexjob.payment.domain.model.Payment;
import com.flexjob.payment.domain.vo.PaymentId;
import java.util.List;
import java.util.Optional;

public interface LoadPaymentPort {
    Optional<Payment> findById(PaymentId id);
    List<Payment> findByBookingId(Long bookingId);
    boolean existsByBookingId(Long bookingId);
}
