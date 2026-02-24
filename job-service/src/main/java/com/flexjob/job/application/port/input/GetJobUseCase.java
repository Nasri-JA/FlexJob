package com.flexjob.job.application.port.input;

import com.flexjob.job.application.dto.response.JobResponse;

import java.util.List;

public interface GetJobUseCase
{
   JobResponse getJobById( Long jobId );

   List<JobResponse> getJobsByEmployerId( Long employerId );

   List<JobResponse> getAllJobs();
}
