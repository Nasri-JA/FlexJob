package com.flexjob.job.application.service;

import com.flexjob.job.application.dto.command.CreateJobCommand;
import com.flexjob.job.application.dto.command.UpdateJobCommand;
import com.flexjob.job.application.dto.response.CategoryResponse;
import com.flexjob.job.application.dto.response.JobResponse;
import com.flexjob.job.application.dto.response.CategoryResponse;
import com.flexjob.job.application.port.input.UpdateJobUseCase;
import com.flexjob.job.application.port.output.LoadCategoryPort;
import com.flexjob.job.application.port.output.LoadJobPort;
import com.flexjob.job.application.port.output.PublishEventPort;
import com.flexjob.job.application.port.output.SaveJobPort;
import com.flexjob.job.domain.exception.CategoryNotFoundException;
import com.flexjob.job.domain.exception.JobNotFoundException;
import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.model.JobCategory;
import com.flexjob.job.domain.model.JobRequirement;
import com.flexjob.job.domain.service.JobDomainService;
import com.flexjob.job.domain.vo.*;
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
public class UpdateJobService implements UpdateJobUseCase
{

   private final JobDomainService jobDomainService;
   private final LoadJobPort loadJobPort;
   private final LoadCategoryPort loadCategoryPort;
   private final SaveJobPort saveJobPort;
   private final PublishEventPort publishEventPort;

   @Override
   public JobResponse updateJob( UpdateJobCommand command )
   {
      log.info( "Updating job ID {} for employer {}", command.getJobId(), command.getEmployerId() );
      JobId jobId = JobId.of( command.getJobId() );
      Job job = loadJobPort.findById( jobId )
                           .orElseThrow( () -> JobNotFoundException.byId( command.getJobId() ) );

      log.debug( "Found job: {} (status={})", job.getTitle().getValue(), job.getStatus() );
      if ( !job.getEmployerId().equals( command.getEmployerId() ) )
      {
         log.error( "Authorization failed: Job {} does not belong to employer {}",
                    command.getJobId(), command.getEmployerId() );
         throw new SecurityException( "You are not authorized to update this job" );
      }

      log.debug( "Authorization check passed" );
      if ( !job.isEditable() )
      {
         log.error( "Cannot update job with status: {}", job.getStatus() );
         throw new IllegalStateException( "Cannot update job with status: " + job.getStatus() );
      }
      JobCategory category = loadCategoryPort.findById( command.getCategoryId() )
                                             .orElseThrow( () -> CategoryNotFoundException.byId( command.getCategoryId() ) );

      log.debug( "Found category: {} (id={})", category.getName(), category.getId() );
      JobTitle title = JobTitle.of( command.getTitle() );
      JobDescription description = JobDescription.of( command.getDescription() );
      Location location = Location.of( command.getLocation() );
      HourlyRate hourlyRate = HourlyRate.of( command.getHourlyRate() );
      JobDuration duration = JobDuration.ofHours( command.getDurationHours() );

      log.debug( "Value Objects created successfully" );
      jobDomainService.updateJobDetails( job, category, title, description, location, hourlyRate, duration );

      log.debug( "Job details updated" );
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
         log.debug( "Updated {} requirements", command.getRequirements().size() );
      }
      Job updatedJob = saveJobPort.save( job );

      log.info( "Job updated successfully with ID: {}", updatedJob.getId().getValue() );
      try
      {
         publishEventPort.publishJobUpdated( updatedJob );
         log.info( "Published JobUpdated event for job ID: {}", updatedJob.getId().getValue() );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish JobUpdated event for job ID: {}", updatedJob.getId().getValue(), e );
      }
      JobResponse response = mapToResponse( updatedJob );

      log.info( "Job update completed successfully. Job ID: {}", updatedJob.getId().getValue() );

      return response;
   }

   private JobResponse mapToResponse( Job job )
   {
      List<JobResponse.RequirementResponse> requirementResponses = job.getRequirements().stream()
                                                                      .map( req -> JobResponse.RequirementResponse.builder()
                                                                                                                  .id(
                                                                                                                     req.getId() )
                                                                                                                  .requirementText(
                                                                                                                     req.getRequirementText() )
                                                                                                                  .isMandatory(
                                                                                                                     req.isMandatory() )
                                                                                                                  .build() )
                                                                      .toList();

      CategoryResponse categoryResponse = CategoryResponse.builder()
                                                          .id( job.getCategory().getId() )
                                                          .name( job.getCategory().getName() )
                                                          .description( job.getCategory().getDescription() )
                                                          .build();

      double totalCompensation = jobDomainService.calculateTotalCompensation( job );
      int qualityScore = jobDomainService.calculateJobQualityScore( job );

      return JobResponse.builder()
                        .id( job.getId().getValue() )
                        .employerId( job.getEmployerId() )
                        .category( categoryResponse )
                        .title( job.getTitle().getValue() )
                        .description( job.getDescription().getValue() )
                        .location( job.getLocation().getValue() )
                        .hourlyRate( job.getHourlyRate().getValueAsDouble() )
                        .durationHours( job.getDuration().getHours() )
                        .status( job.getStatus() )
                        .requirements( requirementResponses )
                        .createdAt( job.getCreatedAt() )
                        .updatedAt( job.getUpdatedAt() )
                        .estimatedTotalCompensation( totalCompensation )
                        .isPremium( job.isPremium() )
                        .isOpenForApplications( job.isOpenForApplications() )
                        .qualityScore( qualityScore )
                        .build();
   }
}
