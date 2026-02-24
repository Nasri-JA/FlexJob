package com.flexjob.application.infrastructure.adapter.output.rest;

import com.flexjob.application.application.port.output.JobServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobServiceRestAdapter implements JobServicePort {

    private final RestTemplate restTemplate;

    @Value("${job-service.url:http://localhost:8082}")
    private String jobServiceUrl;

    @Override
    public JobInfo getJobById(Long jobId) {
        try {
            String url = jobServiceUrl + "/api/jobs/" + jobId;
            JobDto response = restTemplate.getForObject(url, JobDto.class);

            if (response != null) {
                return new JobInfo(
                        response.getId(),
                        response.getEmployerId(),
                        response.getTitle(),
                        response.getStatus()
                );
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to fetch job {}", jobId, e);
            return null;
        }
    }

    @Override
    public boolean jobExists(Long jobId) {
        return getJobById(jobId) != null;
    }

    private static class JobDto {
        private Long id;
        private Long employerId;
        private String title;
        private String status;

        public Long getId() { return id; }
        public Long getEmployerId() { return employerId; }
        public String getTitle() { return title; }
        public String getStatus() { return status; }
    }
}
