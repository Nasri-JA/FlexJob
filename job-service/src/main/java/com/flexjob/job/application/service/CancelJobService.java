package com.flexjob.job.application.service;

import com.flexjob.job.application.port.input.CancelJobUseCase;
import com.flexjob.job.application.port.output.LoadJobPort;
import com.flexjob.job.application.port.output.PublishEventPort;
import com.flexjob.job.application.port.output.SaveJobPort;
import com.flexjob.job.domain.exception.JobNotFoundException;
import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.vo.JobId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CancelJobService implements CancelJobUseCase
{

   private final LoadJobPort loadJobPort;
   private final SaveJobPort saveJobPort;
   private final PublishEventPort publishEventPort;

   @Override
   public void cancelJob( Long jobId, Long employerId )
   {
      log.info( "Cancelling job ID {} for employer {}", jobId, employerId );
      JobId id = JobId.of( jobId );
      Job job = loadJobPort.findById( id )
                           .orElseThrow( () -> JobNotFoundException.byId( jobId ) );

      log.debug( "Found job: {} (status={})", job.getTitle().getValue(), job.getStatus() );
      if ( !job.getEmployerId().equals( employerId ) )
      {
         log.error( "Authorization failed: Job {} does not belong to employer {}",
                    jobId, employerId );
         throw new SecurityException( "You are not authorized to cancel this job" );
      }

      log.debug( "Authorization check passed" );
      try
      {
         job.cancel(); // Wirft IllegalStateException wenn nicht stornierbar
         log.debug( "Job status changed to CANCELLED" );
      }
      catch ( IllegalStateException e )
      {
         log.error( "Cannot cancel job: {}", e.getMessage() );
         throw e; // Re-throw mit Original-Message
      }
      Job cancelledJob = saveJobPort.save( job );

      log.info( "Job cancelled successfully with ID: {}", cancelledJob.getId().getValue() );
      try
      {
         publishEventPort.publishJobCancelled( cancelledJob );
         log.info( "Published JobCancelled event for job ID: {}", cancelledJob.getId().getValue() );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish JobCancelled event for job ID: {}",
                    cancelledJob.getId().getValue(), e );
         // Event-Publishing-Fehler sollten Stornierung nicht fehlschlagen lassen
      }

      log.info( "Job cancellation completed successfully. Job ID: {}", cancelledJob.getId().getValue() );
   }
}
