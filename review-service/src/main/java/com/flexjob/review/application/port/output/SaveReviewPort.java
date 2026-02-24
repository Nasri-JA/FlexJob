package com.flexjob.review.application.port.output;

import com.flexjob.review.domain.model.Review;

public interface SaveReviewPort {
    Review save(Review review);
}
