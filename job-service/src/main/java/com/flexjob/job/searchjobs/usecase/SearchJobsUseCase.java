package com.flexjob.job.searchjobs.usecase;

import com.flexjob.job.searchjobs.dto.SearchJobsCommand;
import com.flexjob.job.shared.dto.JobResponse;

import java.util.List;

public interface SearchJobsUseCase
{
   List<JobResponse> searchJobs( SearchJobsCommand command );
}
