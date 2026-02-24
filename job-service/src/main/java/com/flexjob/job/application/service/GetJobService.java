package com.flexjob.job.application.service;

import com.flexjob.job.application.dto.response.CategoryResponse;
import com.flexjob.job.application.dto.response.JobResponse;
import com.flexjob.job.application.dto.response.CategoryResponse;
import com.flexjob.job.application.port.input.GetJobUseCase;
import com.flexjob.job.application.port.output.LoadJobPort;
import com.flexjob.job.domain.exception.JobNotFoundException;
import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.service.JobDomainService;
import com.flexjob.job.domain.vo.JobId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional( readOnly = true )
public class GetJobService implements GetJobUseCase
{

   private final JobDomainService jobDomainService;
   private final LoadJobPort loadJobPort;

   @Override
   public JobResponse getJobById( Long jobId )
   {
      log.debug( "Fetching job with ID: {}", jobId );

      // Job laden
      JobId id = JobId.of( jobId );
      Job job = loadJobPort.findById( id )
                           .orElseThrow( () -> JobNotFoundException.byId( jobId ) );

      log.debug( "Found job: {} (status={})", job.getTitle().getValue(), job.getStatus() );

      // Response erstellen
      return mapToResponse( job );
   }

   @Override
   public List<JobResponse> getJobsByEmployerId( Long employerId )
   {
      log.debug( "Fetching jobs for employer: {}", employerId );

      List<Job> jobs = loadJobPort.findByEmployerId( employerId );

      log.debug( "Found {} jobs for employer {}", jobs.size(), employerId );

      return jobs.stream()
                 .map( this :: mapToResponse )
                 .toList();
   }

   @Override
   public List<JobResponse> getAllJobs()
   {
      log.debug( "Fetching all jobs" );

      List<Job> jobs = loadJobPort.findAll();

      log.debug( "Found {} jobs", jobs.size() );

      return jobs.stream()
                 .map( this :: mapToResponse )
                 .toList();
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
