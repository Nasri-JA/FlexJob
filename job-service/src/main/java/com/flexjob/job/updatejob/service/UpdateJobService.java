package com.flexjob.job.updatejob.service;

import com.flexjob.job.createjob.dto.CreateJobCommand;
import com.flexjob.job.domain.exception.CategoryNotFoundException;
import com.flexjob.job.domain.exception.JobNotFoundException;
import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.model.JobCategory;
import com.flexjob.job.domain.model.JobRequirement;
import com.flexjob.job.domain.service.JobDomainService;
import com.flexjob.job.domain.vo.*;
import com.flexjob.job.shared.dto.JobResponse;
import com.flexjob.job.shared.mapper.JobResponseMapper;
import com.flexjob.job.shared.repository.CategoryRepository;
import com.flexjob.job.shared.repository.EventPublisher;
import com.flexjob.job.shared.repository.JobRepository;
import com.flexjob.job.updatejob.dto.UpdateJobCommand;
import com.flexjob.job.updatejob.usecase.UpdateJobUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UpdateJobService implements UpdateJobUseCase
{

   private final JobDomainService jobDomainService;
   private final JobRepository jobRepository;
   private final CategoryRepository categoryRepository;
   private final EventPublisher eventPublisher;
   private final JobResponseMapper jobResponseMapper;

   @Override
   public JobResponse updateJob( UpdateJobCommand command )
   {
      log.info( "Updating job ID {} for employer {}", command.getJobId(), command.getEmployerId() );

      JobId jobId = JobId.of( command.getJobId() );
      Job job = jobRepository.findById( jobId )
                             .orElseThrow( () -> JobNotFoundException.byId( command.getJobId() ) );

      if ( !job.getEmployerId().equals( command.getEmployerId() ) )
      {
         throw new SecurityException( "You are not authorized to update this job" );
      }

      if ( !job.isEditable() )
      {
         throw new IllegalStateException( "Cannot update job with status: " + job.getStatus() );
      }

      JobCategory category = categoryRepository.findById( command.getCategoryId() )
                                               .orElseThrow( () -> CategoryNotFoundException.byId( command.getCategoryId() ) );

      JobTitle title = JobTitle.of( command.getTitle() );
      JobDescription description = JobDescription.of( command.getDescription() );
      Location location = Location.of( command.getLocation() );
      HourlyRate hourlyRate = HourlyRate.of( command.getHourlyRate() );
      JobDuration duration = JobDuration.ofHours( command.getDurationHours() );

      jobDomainService.updateJobDetails( job, category, title, description, location, hourlyRate, duration );

      job.clearRequirements();
      if ( command.getRequirements() != null && !command.getRequirements().isEmpty() )
      {
         for ( CreateJobCommand.RequirementCommand reqCmd : command.getRequirements() )
         {
            JobRequirement requirement = JobRequirement.create(
               reqCmd.getRequirementText(),
               reqCmd.getIsMandatory()
            );
            job.addRequirement( requirement );
         }
      }

      Job updatedJob = jobRepository.save( job );

      log.info( "Job updated successfully with ID: {}", updatedJob.getId().getValue() );

      try
      {
         eventPublisher.publishJobUpdated( updatedJob );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish JobUpdated event for job ID: {}", updatedJob.getId().getValue(), e );
      }

      return jobResponseMapper.toJobResponse( updatedJob );
   }
}
