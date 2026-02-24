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
public class BookingCompletedEvent implements Serializable {
    private Long bookingId;
    private Long jobId;
    private Long employeeId;
    private Long employerId;
    private Double totalAmount;
    private Double totalHours;
    private LocalDateTime completedAt;
}
