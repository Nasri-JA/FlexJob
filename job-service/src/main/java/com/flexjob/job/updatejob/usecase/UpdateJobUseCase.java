package com.flexjob.job.updatejob.usecase;

import com.flexjob.job.shared.dto.JobResponse;
import com.flexjob.job.updatejob.dto.UpdateJobCommand;

public interface UpdateJobUseCase
{
   JobResponse updateJob( UpdateJobCommand command );
}
