package com.flexjob.job.canceljob.usecase;

public interface CancelJobUseCase
{
   void cancelJob( Long jobId, Long employerId );
}
