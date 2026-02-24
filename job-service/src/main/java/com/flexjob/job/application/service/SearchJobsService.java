package com.flexjob.job.application.service;

import com.flexjob.job.application.dto.command.SearchJobsCommand;
import com.flexjob.job.application.dto.response.CategoryResponse;
import com.flexjob.job.application.dto.response.JobResponse;
import com.flexjob.job.application.dto.response.CategoryResponse;
import com.flexjob.job.application.port.input.SearchJobsUseCase;
import com.flexjob.job.application.port.output.LoadJobPort;
import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.service.JobDomainService;
import com.flexjob.job.domain.vo.HourlyRate;
import com.flexjob.job.domain.vo.JobDuration;
import com.flexjob.job.domain.vo.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional( readOnly = true )
public class SearchJobsService implements SearchJobsUseCase
{

   private final JobDomainService jobDomainService;
   private final LoadJobPort loadJobPort;

   @Override
   public List<JobResponse> searchJobs( SearchJobsCommand command )
   {
      log.debug( "Searching jobs with criteria: {}", command );
      Stream<Job> jobStream = loadInitialJobs( command );
      jobStream = applyFilters( jobStream, command );
      List<Job> jobs = jobStream
         .sorted( Comparator
                     .comparingInt( ( Job job ) -> jobDomainService.calculateJobQualityScore( job ) )
                     .reversed() // Höchster Score zuerst
                     .thenComparing( Job :: getCreatedAt, Comparator.reverseOrder() ) ) // Neueste zuerst
         .toList();

      log.debug( "Found {} jobs matching criteria", jobs.size() );
      return jobs.stream()
                 .map( this :: mapToResponse )
                 .toList();
   }

   private Stream<Job> loadInitialJobs( SearchJobsCommand command )
   {
      // Optimierung 1: Nur Employer-Jobs
      if ( command.getEmployerId() != null )
      {
         log.debug( "Loading jobs for employer: {}", command.getEmployerId() );
         return loadJobPort.findByEmployerId( command.getEmployerId() ).stream();
      }

      // Optimierung 2: Nur Status-Filter (und keine anderen Filter)
      if ( command.getStatus() != null && command.getCategoryId() == null )
      {
         log.debug( "Loading jobs with status: {}", command.getStatus() );
         return loadJobPort.findByStatus( command.getStatus() ).stream();
      }

      // Optimierung 3: Nur Category-Filter (und keine anderen Filter)
      if ( command.getCategoryId() != null && command.getStatus() == null )
      {
         log.debug( "Loading jobs in category: {}", command.getCategoryId() );
         return loadJobPort.findByCategoryId( command.getCategoryId() ).stream();
      }

      // Default: Alle Jobs laden
      log.debug( "Loading all jobs for filtering" );
      return loadJobPort.findAll().stream();
   }

   private Stream<Job> applyFilters( Stream<Job> jobStream, SearchJobsCommand command )
   {
      // Filter: Status
      if ( command.getStatus() != null )
      {
         jobStream = jobStream.filter( job -> job.getStatus() == command.getStatus() );
      }

      // Filter: Category
      if ( command.getCategoryId() != null )
      {
         jobStream = jobStream.filter( job ->
                                          job.getCategory().getId().equals( command.getCategoryId() )
         );
      }

      // Filter: Location (enthält, Case-Insensitive)
      if ( command.getLocation() != null && !command.getLocation().isEmpty() )
      {
         String searchLocation = command.getLocation().toLowerCase();
         jobStream = jobStream.filter( job ->
                                          job.getLocation().getValue().toLowerCase()
                                             .contains( searchLocation )
         );
      }

      // Filter: Mindest-Stundensatz
      if ( command.getMinRate() != null )
      {
         BigDecimal minRate = BigDecimal.valueOf( command.getMinRate() );
         jobStream = jobStream.filter( job ->
                                          job.getHourlyRate().getValue().compareTo( minRate ) >= 0
         );
      }

      // Filter: Maximal-Stundensatz
      if ( command.getMaxRate() != null )
      {
         BigDecimal maxRate = BigDecimal.valueOf( command.getMaxRate() );
         jobStream = jobStream.filter( job ->
                                          job.getHourlyRate().getValue().compareTo( maxRate ) <= 0
         );
      }

      // Filter: Mindest-Dauer
      if ( command.getMinDurationHours() != null )
      {
         jobStream = jobStream.filter( job ->
                                          job.getDuration().getHours() >= command.getMinDurationHours()
         );
      }

      // Filter: Maximal-Dauer
      if ( command.getMaxDurationHours() != null )
      {
         jobStream = jobStream.filter( job ->
                                          job.getDuration().getHours() <= command.getMaxDurationHours()
         );
      }

      // Filter: Keywords (Titel ODER Beschreibung enthält)
      if ( command.getKeywords() != null && !command.getKeywords().isEmpty() )
      {
         String searchKeywords = command.getKeywords().toLowerCase();
         jobStream = jobStream.filter( job ->
                                          job.getTitle().getValue().toLowerCase().contains( searchKeywords )
                                             ||
                                             job.getDescription().getValue().toLowerCase()
                                                .contains( searchKeywords )
         );
      }

      return jobStream;
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
