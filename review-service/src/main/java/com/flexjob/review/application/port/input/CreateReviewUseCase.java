package com.flexjob.review.application.port.input;

import com.flexjob.review.application.dto.command.CreateReviewCommand;
import com.flexjob.review.application.dto.response.ReviewResponse;

public interface CreateReviewUseCase {
    ReviewResponse createReview(CreateReviewCommand command);
}
