package com.flexjob.engagement.review.infrastructure.adapter.output.messaging;

import com.flexjob.common.events.ReviewCreatedEvent;
import com.flexjob.engagement.review.application.port.output.PublishReviewEventPort;
import com.flexjob.engagement.review.domain.model.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewEventPublisher implements PublishReviewEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishReviewCreated(Review review) {
        try {
            ReviewCreatedEvent event = ReviewCreatedEvent.builder()
                    .reviewId(review.getId().getValue())
                    .bookingId(review.getBookingId())
                    .reviewerId(review.getReviewerId())
                    .revieweeId(review.getRevieweeId())
                    .rating(review.getRating().getValue())
                    .createdAt(review.getCreatedAt())
                    .build();
            kafkaTemplate.send("review-created", event);
        } catch (Exception e) {
            log.error("Failed to publish event", e);
        }
    }
}
