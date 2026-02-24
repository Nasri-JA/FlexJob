package com.flexjob.review.domain.exception;

public class ReviewNotFoundException extends RuntimeException {
    public static ReviewNotFoundException byId(Long reviewId) {
        return new ReviewNotFoundException("Review not found: " + reviewId);
    }

    private ReviewNotFoundException(String message) {
        super(message);
    }
}
