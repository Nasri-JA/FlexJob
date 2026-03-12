package com.flexjob.job.shared.repository;

import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.model.JobStatus;
import com.flexjob.job.domain.vo.JobId;

import java.util.List;
import java.util.Optional;

public interface JobRepository
{
   Optional<Job> findById( JobId jobId );

   List<Job> findByEmployerId( Long employerId );

   List<Job> findByStatus( JobStatus status );

   List<Job> findByCategoryId( Long categoryId );

   List<Job> findAll();

   boolean existsById( JobId jobId );

   long countByEmployerId( Long employerId );

   long countByStatus( JobStatus status );

   Job save( Job job );

   void delete( JobId jobId );

   boolean isOwnedByEmployer( JobId jobId, Long employerId );
}
