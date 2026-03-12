package com.flexjob.job.createjob.controller;

import com.flexjob.job.createjob.dto.CreateJobCommand;
import com.flexjob.job.createjob.usecase.CreateJobUseCase;
import com.flexjob.job.shared.dto.JobResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/jobs" )
@RequiredArgsConstructor
@Slf4j
public class CreateJobController
{
   private final CreateJobUseCase createJobUseCase;

   @PostMapping
   public ResponseEntity<JobResponse> createJob(
      @Valid @RequestBody CreateJobCommand command,
      @RequestHeader( "X-User-Id" ) Long employerId )
   {
      log.info( "POST /api/jobs - Creating job for employer: {}", employerId );

      command.setEmployerId( employerId );
      JobResponse response = createJobUseCase.createJob( command );

      return ResponseEntity.status( HttpStatus.CREATED ).body( response );
   }
}
