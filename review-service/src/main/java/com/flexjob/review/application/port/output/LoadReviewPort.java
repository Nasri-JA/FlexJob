package com.flexjob.review.application.port.output;

import com.flexjob.review.domain.model.Review;
import com.flexjob.review.domain.vo.ReviewId;

import java.util.List;
import java.util.Optional;

public interface LoadReviewPort {
    Optional<Review> findById(ReviewId reviewId);
    List<Review> findByRevieweeId(Long revieweeId);
    List<Review> findByBookingId(Long bookingId);
    boolean existsByBookingIdAndReviewerId(Long bookingId, Long reviewerId);
    double calculateAverageRating(Long revieweeId);
}
