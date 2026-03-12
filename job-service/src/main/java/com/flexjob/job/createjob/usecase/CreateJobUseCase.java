package com.flexjob.job.createjob.usecase;

import com.flexjob.job.createjob.dto.CreateJobCommand;
import com.flexjob.job.shared.dto.JobResponse;

public interface CreateJobUseCase
{
   JobResponse createJob( CreateJobCommand command );
}
