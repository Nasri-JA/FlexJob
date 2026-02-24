package com.flexjob.payment.application.port.output;

import com.flexjob.payment.domain.model.Payment;

public interface SavePaymentPort {
    Payment save(Payment payment);
}
