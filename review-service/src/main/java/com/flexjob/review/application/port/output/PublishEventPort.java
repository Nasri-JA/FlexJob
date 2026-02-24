package com.flexjob.review.application.port.output;

import com.flexjob.review.domain.model.Review;

public interface PublishEventPort {
    void publishReviewCreated(Review review);
}
