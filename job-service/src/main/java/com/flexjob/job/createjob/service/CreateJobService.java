package com.flexjob.job.createjob.service;

import com.flexjob.job.createjob.dto.CreateJobCommand;
import com.flexjob.job.createjob.usecase.CreateJobUseCase;
import com.flexjob.job.domain.exception.CategoryNotFoundException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreateJobService implements CreateJobUseCase
{

   private final JobDomainService jobDomainService;
   private final CategoryRepository categoryRepository;
   private final JobRepository jobRepository;
   private final EventPublisher eventPublisher;
   private final JobResponseMapper jobResponseMapper;

   @Override
   public JobResponse createJob( CreateJobCommand command )
   {
      log.info( "Creating job with title '{}' for employer {}", command.getTitle(), command.getEmployerId() );

      JobCategory category = categoryRepository.findById( command.getCategoryId() )
                                               .orElseThrow( () -> CategoryNotFoundException.byId( command.getCategoryId() ) );

      JobTitle title = JobTitle.of( command.getTitle() );
      JobDescription description = JobDescription.of( command.getDescription() );
      Location location = Location.of( command.getLocation() );
      HourlyRate hourlyRate = HourlyRate.of( command.getHourlyRate() );
      JobDuration duration = JobDuration.ofHours( command.getDurationHours() );

      List<JobRequirement> requirements = new ArrayList<>();
      if ( command.getRequirements() != null && !command.getRequirements().isEmpty() )
      {
         for ( CreateJobCommand.RequirementCommand reqCmd : command.getRequirements() )
         {
            JobRequirement requirement = JobRequirement.create(
               reqCmd.getRequirementText(),
               reqCmd.getIsMandatory()
            );
            requirements.add( requirement );
         }
      }

      Job job = jobDomainService.createJob(
         command.getEmployerId(),
         category,
         title,
         description,
         location,
         hourlyRate,
         duration,
         requirements
      );

      Job savedJob = jobRepository.save( job );

      log.info( "Job saved successfully with ID: {}", savedJob.getId().getValue() );

      try
      {
         eventPublisher.publishJobCreated( savedJob );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish JobCreated event for job ID: {}", savedJob.getId().getValue(), e );
      }

      return jobResponseMapper.toJobResponse( savedJob );
   }
}
