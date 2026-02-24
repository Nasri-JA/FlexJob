package com.flexjob.payment.application.dto.command;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class ProcessPaymentCommand {
    private final Long bookingId;
    private final Long employeeId;
    private final Long employerId;
    private final BigDecimal amount;
}
