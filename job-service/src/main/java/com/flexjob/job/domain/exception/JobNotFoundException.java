package com.flexjob.job.domain.exception;

import com.flexjob.job.domain.vo.JobId;

public class JobNotFoundException extends RuntimeException
{

   private final JobId jobId;

   private JobNotFoundException( String message, JobId jobId )
   {
      super( message );
      this.jobId = jobId;
   }

   private JobNotFoundException( String message )
   {
      super( message );
      this.jobId = null;
   }

   public static JobNotFoundException byId( JobId jobId )
   {
      String message = String.format( "Job not found with ID: %s", jobId.getValue() );
      return new JobNotFoundException( message, jobId );
   }

   public static JobNotFoundException byId( Long jobId )
   {
      String message = String.format( "Job not found with ID: %d", jobId );
      return new JobNotFoundException( message, JobId.of( jobId ) );
   }

   public static JobNotFoundException byEmployer( Long employerId )
   {
      String message = String.format( "No jobs found for employer ID: %d", employerId );
      return new JobNotFoundException( message );
   }

   public static JobNotFoundException withMessage( String message )
   {
      return new JobNotFoundException( message );
   }

   public JobId getJobId()
   {
      return jobId;
   }

   public boolean hasJobId()
   {
      return jobId != null;
   }
}
