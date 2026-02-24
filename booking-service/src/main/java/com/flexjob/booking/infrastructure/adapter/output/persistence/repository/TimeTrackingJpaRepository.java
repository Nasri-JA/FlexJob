package com.flexjob.booking.infrastructure.adapter.output.persistence.repository;

import com.flexjob.booking.infrastructure.adapter.output.persistence.entity.TimeTrackingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeTrackingJpaRepository extends JpaRepository<TimeTrackingJpaEntity, Long> {
    List<TimeTrackingJpaEntity> findByBooking_Id(Long bookingId);
    Optional<TimeTrackingJpaEntity> findByBooking_IdAndEndTimeIsNull(Long bookingId);
}
