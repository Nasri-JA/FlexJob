package com.flexjob.job.getjob.controller;

import com.flexjob.job.getjob.usecase.GetJobUseCase;
import com.flexjob.job.shared.dto.JobResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/jobs" )
@RequiredArgsConstructor
@Slf4j
public class GetJobController
{
   private final GetJobUseCase getJobUseCase;

   @GetMapping( "/{id}" )
   public ResponseEntity<JobResponse> getJobById( @PathVariable Long id )
   {
      log.info( "GET /api/jobs/{} - Fetching job", id );

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
         responses = getJobUseCase.getJobsByEmployerId( employerId );
      }
      else
      {
         responses = getJobUseCase.getAllJobs();
      }

      return ResponseEntity.ok( responses );
   }
}
