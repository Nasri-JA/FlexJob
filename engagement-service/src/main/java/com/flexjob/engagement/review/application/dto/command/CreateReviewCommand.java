package com.flexjob.engagement.review.application.dto.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateReviewCommand {
    Long bookingId;
    Long reviewerId;
    Long revieweeId;
    Integer rating;
    String comment;
}
