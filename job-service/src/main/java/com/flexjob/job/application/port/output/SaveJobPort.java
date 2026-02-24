package com.flexjob.job.application.port.output;

import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.vo.JobId;

public interface SaveJobPort
{
   Job save( Job job );

   void delete( JobId jobId );

   boolean isOwnedByEmployer( JobId jobId, Long employerId );
}
