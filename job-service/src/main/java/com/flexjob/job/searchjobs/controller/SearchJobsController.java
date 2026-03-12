package com.flexjob.job.searchjobs.controller;

import com.flexjob.job.searchjobs.dto.SearchJobsCommand;
import com.flexjob.job.searchjobs.usecase.SearchJobsUseCase;
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
public class SearchJobsController
{
   private final SearchJobsUseCase searchJobsUseCase;

   @PostMapping( "/search" )
   public ResponseEntity<List<JobResponse>> searchJobs(
      @RequestBody SearchJobsCommand command )
   {
      log.info( "POST /api/jobs/search - Searching jobs" );

      List<JobResponse> responses = searchJobsUseCase.searchJobs( command );

      return ResponseEntity.ok( responses );
   }
}
