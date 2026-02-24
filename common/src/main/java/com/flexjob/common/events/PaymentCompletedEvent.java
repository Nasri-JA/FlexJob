package com.flexjob.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent implements Serializable {
    private Long paymentId;
    private Long bookingId;
    private Long employeeId;
    private Long employerId;
    private Double amount;
    private String paymentMethod;
    private LocalDateTime completedAt;
}
