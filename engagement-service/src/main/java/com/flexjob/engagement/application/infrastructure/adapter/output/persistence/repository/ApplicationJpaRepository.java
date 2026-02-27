package com.flexjob.engagement.application.infrastructure.adapter.output.persistence.repository;

import com.flexjob.engagement.application.enums.ApplicationStatus;
import com.flexjob.engagement.application.infrastructure.adapter.output.persistence.entity.ApplicationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationJpaRepository extends JpaRepository<ApplicationJpaEntity, Long> {
    List<ApplicationJpaEntity> findByJobId(Long jobId);
    List<ApplicationJpaEntity> findByEmployeeId(Long employeeId);
    List<ApplicationJpaEntity> findByEmployerId(Long employerId);
    List<ApplicationJpaEntity> findByStatus(ApplicationStatus status);
    boolean existsByJobIdAndEmployeeId(Long jobId, Long employeeId);
}
