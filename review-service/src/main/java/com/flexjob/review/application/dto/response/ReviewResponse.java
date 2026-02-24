package com.flexjob.review.application.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ReviewResponse {
    Long id;
    Long bookingId;
    Long reviewerId;
    Long revieweeId;
    Integer rating;
    String comment;
    LocalDateTime createdAt;
}
