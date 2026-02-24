package com.flexjob.payment.application.port.input;

import com.flexjob.payment.application.dto.command.ProcessPaymentCommand;
import com.flexjob.payment.application.dto.response.PaymentResponse;

public interface ProcessPaymentUseCase {
    PaymentResponse processPayment(ProcessPaymentCommand command);
}
