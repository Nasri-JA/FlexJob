package com.flexjob.engagement.review.application.port.output;

import com.flexjob.engagement.review.domain.model.Review;

public interface PublishReviewEventPort {
    void publishReviewCreated(Review review);
}
