package com.flexjob.review.domain.service;

import com.flexjob.review.domain.model.Review;
import com.flexjob.review.domain.vo.Rating;
import com.flexjob.review.domain.vo.ReviewComment;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewDomainService {

    public Review createReview(Long bookingId, Long reviewerId, Long revieweeId,
                              Rating rating, ReviewComment comment) {
        if (bookingId == null || bookingId <= 0) {
            throw new IllegalArgumentException("Invalid booking ID");
        }
        if (reviewerId == null || reviewerId <= 0) {
            throw new IllegalArgumentException("Invalid reviewer ID");
        }
        if (revieweeId == null || revieweeId <= 0) {
            throw new IllegalArgumentException("Invalid reviewee ID");
        }
        if (reviewerId.equals(revieweeId)) {
            throw new IllegalArgumentException("Cannot review yourself");
        }

        return Review.builder()
                .bookingId(bookingId)
                .reviewerId(reviewerId)
                .revieweeId(revieweeId)
                .rating(rating)
                .comment(comment)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(r -> r.getRating().getValue())
                .average()
                .orElse(0.0);
    }
}
