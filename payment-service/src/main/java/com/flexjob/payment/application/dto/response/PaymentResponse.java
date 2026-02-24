package com.flexjob.payment.application.dto.response;

import com.flexjob.payment.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {
    private final Long id;
    private final Long bookingId;
    private final Long employeeId;
    private final Long employerId;
    private final PaymentStatus status;
    private final BigDecimal amount;
    private final String currency;
    private final LocalDateTime createdAt;
}
