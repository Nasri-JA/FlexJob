package com.flexjob.engagement.review.application.port.input;

import com.flexjob.engagement.review.application.dto.command.CreateReviewCommand;
import com.flexjob.engagement.review.application.dto.response.ReviewResponse;

public interface CreateReviewUseCase {
    ReviewResponse createReview(CreateReviewCommand command);
}
