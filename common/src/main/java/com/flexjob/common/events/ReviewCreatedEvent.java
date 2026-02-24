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
public class ReviewCreatedEvent implements Serializable {
    private Long reviewId;
    private Long bookingId;
    private Long reviewerId;
    private Long revieweeId;
    private Integer rating;
    private LocalDateTime createdAt;
}
