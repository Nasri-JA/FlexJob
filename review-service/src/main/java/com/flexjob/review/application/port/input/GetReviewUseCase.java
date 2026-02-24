package com.flexjob.review.application.port.input;

import com.flexjob.review.application.dto.response.ReviewResponse;

import java.util.List;

public interface GetReviewUseCase {
    ReviewResponse getReviewById(Long reviewId);
    List<ReviewResponse> getReviewsForUser(Long userId);
    double getAverageRating(Long userId);
}
