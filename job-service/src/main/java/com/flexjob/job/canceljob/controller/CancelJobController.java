package com.flexjob.job.canceljob.controller;

import com.flexjob.job.canceljob.usecase.CancelJobUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/jobs" )
@RequiredArgsConstructor
@Slf4j
public class CancelJobController
{
   private final CancelJobUseCase cancelJobUseCase;

   @DeleteMapping( "/{id}" )
   public ResponseEntity<Void> cancelJob(
      @PathVariable Long id,
      @RequestHeader( "X-User-Id" ) Long employerId )
   {
      log.info( "DELETE /api/jobs/{} - Cancelling job for employer: {}", id, employerId );

      cancelJobUseCase.cancelJob( id, employerId );

      return ResponseEntity.noContent().build();
   }
}
