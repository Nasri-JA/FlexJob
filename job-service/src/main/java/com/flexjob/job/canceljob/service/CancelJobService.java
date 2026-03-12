package com.flexjob.job.canceljob.service;

import com.flexjob.job.canceljob.usecase.CancelJobUseCase;
import com.flexjob.job.domain.exception.JobNotFoundException;
import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.vo.JobId;
import com.flexjob.job.shared.repository.EventPublisher;
import com.flexjob.job.shared.repository.JobRepository;
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

   private final JobRepository jobRepository;
   private final EventPublisher eventPublisher;

   @Override
   public void cancelJob( Long jobId, Long employerId )
   {
      log.info( "Cancelling job ID {} for employer {}", jobId, employerId );

      JobId id = JobId.of( jobId );
      Job job = jobRepository.findById( id )
                             .orElseThrow( () -> JobNotFoundException.byId( jobId ) );

      if ( !job.getEmployerId().equals( employerId ) )
      {
         throw new SecurityException( "You are not authorized to cancel this job" );
      }

      job.cancel();

      Job cancelledJob = jobRepository.save( job );

      log.info( "Job cancelled successfully with ID: {}", cancelledJob.getId().getValue() );

      try
      {
         eventPublisher.publishJobCancelled( cancelledJob );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish JobCancelled event for job ID: {}",
                    cancelledJob.getId().getValue(), e );
      }
   }
}
