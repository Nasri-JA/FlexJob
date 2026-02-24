package com.flexjob.job.application.port.input;

import com.flexjob.job.application.dto.command.SearchJobsCommand;
import com.flexjob.job.application.dto.response.JobResponse;

import java.util.List;

public interface SearchJobsUseCase
{
   List<JobResponse> searchJobs( SearchJobsCommand command );
}
