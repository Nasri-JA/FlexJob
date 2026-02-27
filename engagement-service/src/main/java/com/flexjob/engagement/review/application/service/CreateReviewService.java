package com.flexjob.engagement.review.application.service;

import com.flexjob.engagement.review.application.dto.command.CreateReviewCommand;
import com.flexjob.engagement.review.application.dto.response.ReviewResponse;
import com.flexjob.engagement.review.application.port.input.CreateReviewUseCase;
import com.flexjob.engagement.review.application.port.output.LoadReviewPort;
import com.flexjob.engagement.review.application.port.output.PublishReviewEventPort;
import com.flexjob.engagement.review.application.port.output.SaveReviewPort;
import com.flexjob.engagement.review.domain.model.Review;
import com.flexjob.engagement.review.domain.service.ReviewDomainService;
import com.flexjob.engagement.review.domain.vo.Rating;
import com.flexjob.engagement.review.domain.vo.ReviewComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreateReviewService implements CreateReviewUseCase {

    private final ReviewDomainService reviewDomainService;
    private final LoadReviewPort loadReviewPort;
    private final SaveReviewPort saveReviewPort;
    private final PublishReviewEventPort publishReviewEventPort;

    @Override
    public ReviewResponse createReview(CreateReviewCommand command) {
        if (loadReviewPort.existsByBookingIdAndReviewerId(command.getBookingId(), command.getReviewerId())) {
            throw new IllegalStateException("Review already exists");
        }

        Rating rating = Rating.of(command.getRating());
        ReviewComment comment = ReviewComment.of(command.getComment());

        Review review = reviewDomainService.createReview(
                command.getBookingId(),
                command.getReviewerId(),
                command.getRevieweeId(),
                rating,
                comment
        );

        Review saved = saveReviewPort.save(review);

        try {
            publishReviewEventPort.publishReviewCreated(saved);
        } catch (Exception e) {
            log.error("Failed to publish event", e);
        }

        return mapToResponse(saved);
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
