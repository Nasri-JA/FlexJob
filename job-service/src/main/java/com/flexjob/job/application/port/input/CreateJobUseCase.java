package com.flexjob.job.application.port.input;

import com.flexjob.job.application.dto.command.CreateJobCommand;
import com.flexjob.job.application.dto.response.JobResponse;

public interface CreateJobUseCase
{
   JobResponse createJob( CreateJobCommand command );
}
