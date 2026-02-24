package com.flexjob.job.application.port.input;

public interface CancelJobUseCase
{
   void cancelJob( Long jobId, Long employerId );
}
