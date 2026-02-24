package com.flexjob.job.infrastructure.adapter.input.rest.controller;

import com.flexjob.job.application.dto.command.CreateJobCommand;
import com.flexjob.job.application.dto.command.SearchJobsCommand;
import com.flexjob.job.application.dto.command.UpdateJobCommand;
import com.flexjob.job.application.dto.response.JobResponse;
import com.flexjob.job.application.port.input.*;
import com.flexjob.job.infrastructure.adapter.input.rest.dto.CreateJobRequest;
import com.flexjob.job.infrastructure.adapter.input.rest.dto.SearchJobsRequest;
import com.flexjob.job.infrastructure.adapter.input.rest.mapper.JobRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/jobs" )
@RequiredArgsConstructor
@Slf4j
public class JobController
{
   private final CreateJobUseCase createJobUseCase;
   private final UpdateJobUseCase updateJobUseCase;
   private final GetJobUseCase getJobUseCase;
   private final SearchJobsUseCase searchJobsUseCase;
   private final CancelJobUseCase cancelJobUseCase;
   private final JobRestMapper jobRestMapper;

   @PostMapping
   public ResponseEntity<JobResponse> createJob(
      @Valid @RequestBody CreateJobRequest request,
      @RequestHeader( "X-User-Id" ) Long employerId )
   {

      log.info( "POST /api/jobs - Creating job for employer: {}", employerId );

      // Map REST DTO -> Command
      CreateJobCommand command = jobRestMapper.toCreateCommand( request, employerId );

      // Execute Use Case
      JobResponse response = createJobUseCase.createJob( command );

      log.info( "Job created successfully with ID: {}", response.getId() );

      return ResponseEntity.status( HttpStatus.CREATED ).body( response );
   }

   @GetMapping( "/{id}" )
   public ResponseEntity<JobResponse> getJobById( @PathVariable Long id )
   {
      log.info( "GET /api/jobs/{} - Fetching job", id );

      // Execute Use Case
      JobResponse response = getJobUseCase.getJobById( id );

      return ResponseEntity.ok( response );
   }

   @GetMapping
   public ResponseEntity<List<JobResponse>> getAllJobs(
      @RequestParam( required = false ) Long employerId )
   {

      log.info( "GET /api/jobs - Fetching jobs (employerId={})", employerId );

      List<JobResponse> responses;

      if ( employerId != null )
      {
         // Filter by Employer
         responses = getJobUseCase.getJobsByEmployerId( employerId );
      }
      else
      {
         // All Jobs
         responses = getJobUseCase.getAllJobs();
      }

      log.info( "Found {} jobs", responses.size() );

      return ResponseEntity.ok( responses );
   }

   @PutMapping( "/{id}" )
   public ResponseEntity<JobResponse> updateJob(
      @PathVariable Long id,
      @Valid @RequestBody CreateJobRequest request,
      @RequestHeader( "X-User-Id" ) Long employerId )
   {

      log.info( "PUT /api/jobs/{} - Updating job for employer: {}", id, employerId );

      // Map REST DTO -> Command
      UpdateJobCommand command = jobRestMapper.toUpdateCommand( id, request, employerId );

      // Execute Use Case
      JobResponse response = updateJobUseCase.updateJob( command );

      log.info( "Job updated successfully with ID: {}", response.getId() );

      return ResponseEntity.ok( response );
   }

   @DeleteMapping( "/{id}" )
   public ResponseEntity<Void> cancelJob(
      @PathVariable Long id,
      @RequestHeader( "X-User-Id" ) Long employerId )
   {

      log.info( "DELETE /api/jobs/{} - Cancelling job for employer: {}", id, employerId );

      // Execute Use Case
      cancelJobUseCase.cancelJob( id, employerId );

      log.info( "Job cancelled successfully with ID: {}", id );

      return ResponseEntity.noContent().build();
   }

   @PostMapping( "/search" )
   public ResponseEntity<List<JobResponse>> searchJobs(
      @RequestBody SearchJobsRequest request )
   {

      log.info( "POST /api/jobs/search - Searching jobs with criteria: {}", request );

      // Map REST DTO -> Command
      SearchJobsCommand command = jobRestMapper.toSearchCommand( request );

      // Execute Use Case
      List<JobResponse> responses = searchJobsUseCase.searchJobs( command );

      log.info( "Found {} jobs matching criteria", responses.size() );

      return ResponseEntity.ok( responses );
   }
}
