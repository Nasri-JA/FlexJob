package com.flexjob.job.updatejob.controller;

import com.flexjob.job.shared.dto.JobResponse;
import com.flexjob.job.updatejob.dto.UpdateJobCommand;
import com.flexjob.job.updatejob.usecase.UpdateJobUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/jobs" )
@RequiredArgsConstructor
@Slf4j
public class UpdateJobController
{
   private final UpdateJobUseCase updateJobUseCase;

   @PutMapping( "/{id}" )
   public ResponseEntity<JobResponse> updateJob(
      @PathVariable Long id,
      @Valid @RequestBody UpdateJobCommand command,
      @RequestHeader( "X-User-Id" ) Long employerId )
   {
      log.info( "PUT /api/jobs/{} - Updating job for employer: {}", id, employerId );

      command.setJobId( id );
      command.setEmployerId( employerId );
      JobResponse response = updateJobUseCase.updateJob( command );

      return ResponseEntity.ok( response );
   }
}
