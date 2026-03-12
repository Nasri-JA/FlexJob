package com.flexjob.job.getjob.usecase;

import com.flexjob.job.shared.dto.JobResponse;

import java.util.List;

public interface GetJobUseCase
{
   JobResponse getJobById( Long jobId );

   List<JobResponse> getJobsByEmployerId( Long employerId );

   List<JobResponse> getAllJobs();
}
