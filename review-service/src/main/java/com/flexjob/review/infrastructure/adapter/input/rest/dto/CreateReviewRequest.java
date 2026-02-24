package com.flexjob.review.infrastructure.adapter.input.rest.dto;

import lombok.Data;

@Data
public class CreateReviewRequest {
    private Long bookingId;
    private Long reviewerId;
    private Long revieweeId;
    private Integer rating;
    private String comment;
}
