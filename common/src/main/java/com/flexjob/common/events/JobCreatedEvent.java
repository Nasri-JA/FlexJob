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
public class JobCreatedEvent implements Serializable {
    private Long jobId;
    private Long employerId;
    private String title;
    private String location;
    private Double hourlyRate;
    private LocalDateTime createdAt;
}
