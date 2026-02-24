package com.flexjob.application.application.port.output;

public interface JobServicePort {

    record JobInfo(
            Long id,
            Long employerId,
            String title,
            String status
    ) {}

    JobInfo getJobById(Long jobId);

    boolean jobExists(Long jobId);
}
