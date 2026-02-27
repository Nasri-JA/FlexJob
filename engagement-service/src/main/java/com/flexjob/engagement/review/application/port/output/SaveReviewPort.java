package com.flexjob.engagement.review.application.port.output;

import com.flexjob.engagement.review.domain.model.Review;

public interface SaveReviewPort {
    Review save(Review review);
}
