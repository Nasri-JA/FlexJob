package com.flexjob.job.application.port.input;

import com.flexjob.job.application.dto.command.UpdateJobCommand;
import com.flexjob.job.application.dto.response.JobResponse;

public interface UpdateJobUseCase
{
   JobResponse updateJob( UpdateJobCommand command );
}
