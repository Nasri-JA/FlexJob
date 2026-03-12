package com.flexjob.job.searchjobs.service;

import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.service.JobDomainService;
import com.flexjob.job.searchjobs.dto.SearchJobsCommand;
import com.flexjob.job.searchjobs.usecase.SearchJobsUseCase;
import com.flexjob.job.shared.dto.JobResponse;
import com.flexjob.job.shared.mapper.JobResponseMapper;
import com.flexjob.job.shared.repository.JobRepository;
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
   private final JobRepository jobRepository;
   private final JobResponseMapper jobResponseMapper;

   @Override
   public List<JobResponse> searchJobs( SearchJobsCommand command )
   {
      log.debug( "Searching jobs with criteria: {}", command );

      Stream<Job> jobStream = loadInitialJobs( command );
      jobStream = applyFilters( jobStream, command );

      List<Job> jobs = jobStream
         .sorted( Comparator
                     .comparingInt( ( Job job ) -> jobDomainService.calculateJobQualityScore( job ) )
                     .reversed()
                     .thenComparing( Job :: getCreatedAt, Comparator.reverseOrder() ) )
         .toList();

      log.debug( "Found {} jobs matching criteria", jobs.size() );

      return jobs.stream()
                 .map( jobResponseMapper :: toJobResponse )
                 .toList();
   }

   private Stream<Job> loadInitialJobs( SearchJobsCommand command )
   {
      if ( command.getEmployerId() != null )
      {
         return jobRepository.findByEmployerId( command.getEmployerId() ).stream();
      }

      if ( command.getStatus() != null && command.getCategoryId() == null )
      {
         return jobRepository.findByStatus( command.getStatus() ).stream();
      }

      if ( command.getCategoryId() != null && command.getStatus() == null )
      {
         return jobRepository.findByCategoryId( command.getCategoryId() ).stream();
      }

      return jobRepository.findAll().stream();
   }

   private Stream<Job> applyFilters( Stream<Job> jobStream, SearchJobsCommand command )
   {
      if ( command.getStatus() != null )
      {
         jobStream = jobStream.filter( job -> job.getStatus() == command.getStatus() );
      }

      if ( command.getCategoryId() != null )
      {
         jobStream = jobStream.filter( job ->
                                          job.getCategory().getId().equals( command.getCategoryId() ) );
      }

      if ( command.getLocation() != null && !command.getLocation().isEmpty() )
      {
         String searchLocation = command.getLocation().toLowerCase();
         jobStream = jobStream.filter( job ->
                                          job.getLocation().getValue().toLowerCase().contains( searchLocation ) );
      }

      if ( command.getMinRate() != null )
      {
         BigDecimal minRate = BigDecimal.valueOf( command.getMinRate() );
         jobStream = jobStream.filter( job ->
                                          job.getHourlyRate().getValue().compareTo( minRate ) >= 0 );
      }

      if ( command.getMaxRate() != null )
      {
         BigDecimal maxRate = BigDecimal.valueOf( command.getMaxRate() );
         jobStream = jobStream.filter( job ->
                                          job.getHourlyRate().getValue().compareTo( maxRate ) <= 0 );
      }

      if ( command.getMinDurationHours() != null )
      {
         jobStream = jobStream.filter( job ->
                                          job.getDuration().getHours() >= command.getMinDurationHours() );
      }

      if ( command.getMaxDurationHours() != null )
      {
         jobStream = jobStream.filter( job ->
                                          job.getDuration().getHours() <= command.getMaxDurationHours() );
      }

      if ( command.getKeywords() != null && !command.getKeywords().isEmpty() )
      {
         String searchKeywords = command.getKeywords().toLowerCase();
         jobStream = jobStream.filter( job ->
                                          job.getTitle().getValue().toLowerCase().contains( searchKeywords )
                                             || job.getDescription().getValue().toLowerCase().contains( searchKeywords ) );
      }

      return jobStream;
   }
}
