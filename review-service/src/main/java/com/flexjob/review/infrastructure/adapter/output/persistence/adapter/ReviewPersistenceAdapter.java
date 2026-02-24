package com.flexjob.review.infrastructure.adapter.output.persistence.adapter;

import com.flexjob.review.application.port.output.LoadReviewPort;
import com.flexjob.review.application.port.output.SaveReviewPort;
import com.flexjob.review.domain.model.Review;
import com.flexjob.review.domain.vo.ReviewId;
import com.flexjob.review.infrastructure.adapter.output.persistence.mapper.ReviewPersistenceMapper;
import com.flexjob.review.infrastructure.adapter.output.persistence.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewPersistenceAdapter implements LoadReviewPort, SaveReviewPort {

    private final ReviewJpaRepository repository;
    private final ReviewPersistenceMapper mapper;

    @Override
    public Optional<Review> findById(ReviewId reviewId) {
        return repository.findById(reviewId.getValue()).map(mapper::toDomain);
    }

    @Override
    public List<Review> findByRevieweeId(Long revieweeId) {
        return repository.findByRevieweeId(revieweeId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Review> findByBookingId(Long bookingId) {
        return repository.findByBookingId(bookingId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByBookingIdAndReviewerId(Long bookingId, Long reviewerId) {
        return repository.existsByBookingIdAndReviewerId(bookingId, reviewerId);
    }

    @Override
    public double calculateAverageRating(Long revieweeId) {
        Double avg = repository.calculateAverageRating(revieweeId);
        return avg != null ? avg : 0.0;
    }

    @Override
    public Review save(Review review) {
        return mapper.toDomain(repository.save(mapper.toJpaEntity(review)));
    }
}
