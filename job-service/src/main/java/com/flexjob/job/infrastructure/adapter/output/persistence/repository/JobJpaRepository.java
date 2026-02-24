package com.flexjob.job.infrastructure.adapter.output.persistence.repository;

import com.flexjob.job.enums.JobStatus;
import com.flexjob.job.infrastructure.adapter.output.persistence.entity.JobJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobJpaRepository extends JpaRepository<JobJpaEntity, Long>,
   JpaSpecificationExecutor<JobJpaEntity>
{

   List<JobJpaEntity> findByEmployerId( Long employerId );

   List<JobJpaEntity> findByStatus( JobStatus status );

   List<JobJpaEntity> findByCategoryId( Long categoryId );

   long countByEmployerId( Long employerId );

   long countByStatus( JobStatus status );
}
