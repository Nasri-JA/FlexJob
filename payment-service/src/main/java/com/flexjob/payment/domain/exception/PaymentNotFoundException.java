package com.flexjob.payment.domain.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }

    public static PaymentNotFoundException byId(Long id) {
        return new PaymentNotFoundException("Payment not found with ID: " + id);
    }
}
