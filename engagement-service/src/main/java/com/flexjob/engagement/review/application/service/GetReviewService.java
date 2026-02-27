package com.flexjob.engagement.review.application.service;

import com.flexjob.engagement.review.application.dto.response.ReviewResponse;
import com.flexjob.engagement.review.application.port.input.GetReviewUseCase;
import com.flexjob.engagement.review.application.port.output.LoadReviewPort;
import com.flexjob.engagement.review.domain.exception.ReviewNotFoundException;
import com.flexjob.engagement.review.domain.model.Review;
import com.flexjob.engagement.review.domain.vo.ReviewId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetReviewService implements GetReviewUseCase {

    private final LoadReviewPort loadReviewPort;

    @Override
    public ReviewResponse getReviewById(Long reviewId) {
        Review review = loadReviewPort.findById(ReviewId.of(reviewId))
                .orElseThrow(() -> ReviewNotFoundException.byId(reviewId));
        return mapToResponse(review);
    }

    @Override
    public List<ReviewResponse> getReviewsForUser(Long userId) {
        return loadReviewPort.findByRevieweeId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public double getAverageRating(Long userId) {
        return loadReviewPort.calculateAverageRating(userId);
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId().getValue())
                .bookingId(review.getBookingId())
                .reviewerId(review.getReviewerId())
                .revieweeId(review.getRevieweeId())
                .rating(review.getRating().getValue())
                .comment(review.getComment().getValue())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
