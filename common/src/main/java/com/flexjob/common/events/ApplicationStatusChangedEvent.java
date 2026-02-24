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
public class ApplicationStatusChangedEvent implements Serializable {
    private Long applicationId;
    private Long jobId;
    private Long employeeId;
    private Long employerId;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changedAt;
}
