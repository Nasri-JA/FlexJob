package com.flexjob.booking.infrastructure.adapter.output.persistence.repository;

import com.flexjob.booking.infrastructure.adapter.output.persistence.entity.BookingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingJpaRepository extends JpaRepository<BookingJpaEntity, Long> {
    List<BookingJpaEntity> findByEmployeeId(Long employeeId);
    List<BookingJpaEntity> findByEmployerId(Long employerId);
    List<BookingJpaEntity> findByJobId(Long jobId);
    boolean existsByApplicationId(Long applicationId);
}
