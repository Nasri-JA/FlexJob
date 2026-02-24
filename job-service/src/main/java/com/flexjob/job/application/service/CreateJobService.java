package com.flexjob.job.application.service;

import com.flexjob.job.application.dto.command.CreateJobCommand;
import com.flexjob.job.application.dto.response.CategoryResponse;
import com.flexjob.job.application.dto.response.JobResponse;
import com.flexjob.job.application.port.input.CreateJobUseCase;
import com.flexjob.job.application.port.output.LoadCategoryPort;
import com.flexjob.job.application.port.output.PublishEventPort;
import com.flexjob.job.application.port.output.SaveJobPort;
import com.flexjob.job.domain.exception.CategoryNotFoundException;
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
public class CreateJobService implements CreateJobUseCase
{

   private final JobDomainService jobDomainService;
   private final LoadCategoryPort loadCategoryPort;
   private final SaveJobPort saveJobPort;
   private final PublishEventPort publishEventPort;

   @Override
   public JobResponse createJob( CreateJobCommand command )
   {
      log.info( "Creating job with title '{}' for employer {}", command.getTitle(), command.getEmployerId() );
      JobCategory category = loadCategoryPort.findById( command.getCategoryId() )
                                             .orElseThrow( () -> CategoryNotFoundException.byId( command.getCategoryId() ) );

      log.debug( "Found category: {} (id={})", category.getName(), category.getId() );
      JobTitle title = JobTitle.of( command.getTitle() );
      JobDescription description = JobDescription.of( command.getDescription() );
      Location location = Location.of( command.getLocation() );
      HourlyRate hourlyRate = HourlyRate.of( command.getHourlyRate() );
      JobDuration duration = JobDuration.ofHours( command.getDurationHours() );

      log.debug( "Value Objects created successfully" );
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
         log.debug( "Created {} requirements", requirements.size() );
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

      log.debug( "Job entity created with {} requirements", job.getRequirements().size() );
      Job savedJob = saveJobPort.save( job );

      log.info( "Job saved successfully with ID: {}", savedJob.getId().getValue() );
      try
      {
         publishEventPort.publishJobCreated( savedJob );
         log.info( "Published JobCreated event for job ID: {}", savedJob.getId().getValue() );
      }
      catch ( Exception e )
      {
         // Event-Publishing-Fehler sollten Job-Erstellung nicht fehlschlagen lassen
         log.error( "Failed to publish JobCreated event for job ID: {}", savedJob.getId().getValue(), e );
         // Weiter machen - Job wurde erfolgreich erstellt
      }
      JobResponse response = mapToResponse( savedJob );

      log.info( "Job creation completed successfully. Job ID: {}", savedJob.getId().getValue() );

      return response;
   }

   private JobResponse mapToResponse( Job job )
   {
      // Map Requirements
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

      // Map Category
      CategoryResponse categoryResponse = CategoryResponse.builder()
                                                          .id( job.getCategory().getId() )
                                                          .name( job.getCategory().getName() )
                                                          .description( job.getCategory().getDescription() )
                                                          .build();

      // Berechne Felder
      double totalCompensation = jobDomainService.calculateTotalCompensation( job );
      int qualityScore = jobDomainService.calculateJobQualityScore( job );

      // Build Response
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
