package com.flexjob.job.application.port.output;

import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.vo.JobId;
import com.flexjob.job.enums.JobStatus;

import java.util.List;
import java.util.Optional;

public interface LoadJobPort
{
   Optional<Job> findById( JobId jobId );

   List<Job> findByEmployerId( Long employerId );

   List<Job> findByStatus( JobStatus status );

   List<Job> findByCategoryId( Long categoryId );

   List<Job> findAll();

   boolean existsById( JobId jobId );

   long countByEmployerId( Long employerId );

   long countByStatus( JobStatus status );
}
