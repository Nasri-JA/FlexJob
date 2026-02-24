package com.flexjob.review.infrastructure.adapter.output.persistence.repository;

import com.flexjob.review.infrastructure.adapter.output.persistence.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, Long> {
    List<ReviewJpaEntity> findByRevieweeId(Long revieweeId);
    List<ReviewJpaEntity> findByBookingId(Long bookingId);
    boolean existsByBookingIdAndReviewerId(Long bookingId, Long reviewerId);

    @Query("SELECT AVG(r.rating) FROM ReviewJpaEntity r WHERE r.revieweeId = :revieweeId")
    Double calculateAverageRating(Long revieweeId);
}
