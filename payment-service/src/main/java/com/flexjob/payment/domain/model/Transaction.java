package com.flexjob.payment.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class Transaction {
    private final Long id;
    private final Long paymentId;
    private final String type;
    private final Double amount;
    private final String status;
    private final LocalDateTime createdAt;
}
