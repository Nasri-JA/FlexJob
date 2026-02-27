package com.flexjob.engagement.review.infrastructure.adapter.output.persistence.mapper;

import com.flexjob.engagement.review.domain.model.Review;
import com.flexjob.engagement.review.domain.vo.Rating;
import com.flexjob.engagement.review.domain.vo.ReviewComment;
import com.flexjob.engagement.review.domain.vo.ReviewId;
import com.flexjob.engagement.review.infrastructure.adapter.output.persistence.entity.ReviewJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ReviewPersistenceMapper {

    public Review toDomain(ReviewJpaEntity jpa) {
        if (jpa == null) return null;

        return Review.builder()
                .id(ReviewId.of(jpa.getId()))
                .bookingId(jpa.getBookingId())
                .reviewerId(jpa.getReviewerId())
                .revieweeId(jpa.getRevieweeId())
                .rating(Rating.of(jpa.getRating()))
                .comment(ReviewComment.of(jpa.getComment()))
                .createdAt(jpa.getCreatedAt())
                .build();
    }

    public ReviewJpaEntity toJpaEntity(Review domain) {
        if (domain == null) return null;

        return ReviewJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .bookingId(domain.getBookingId())
                .reviewerId(domain.getReviewerId())
                .revieweeId(domain.getRevieweeId())
                .rating(domain.getRating().getValue())
                .comment(domain.getComment().getValue())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
