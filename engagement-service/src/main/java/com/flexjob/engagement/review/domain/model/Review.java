package com.flexjob.engagement.review.domain.model;

import com.flexjob.engagement.review.domain.vo.Rating;
import com.flexjob.engagement.review.domain.vo.ReviewComment;
import com.flexjob.engagement.review.domain.vo.ReviewId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Review {
    private ReviewId id;
    private Long bookingId;
    private Long reviewerId;
    private Long revieweeId;
    private Rating rating;
    private ReviewComment comment;
    private LocalDateTime createdAt;
}
