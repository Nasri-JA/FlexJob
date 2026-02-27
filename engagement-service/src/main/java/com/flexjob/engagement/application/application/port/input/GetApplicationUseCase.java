package com.flexjob.engagement.application.application.port.input;
import com.flexjob.engagement.application.application.dto.response.ApplicationResponse;
import java.util.List;
public interface GetApplicationUseCase {
    ApplicationResponse getApplicationById(Long applicationId);
    List<ApplicationResponse> getApplicationsForJob(Long jobId);
    List<ApplicationResponse> getApplicationsForEmployee(Long employeeId);
    List<ApplicationResponse> getApplicationsForEmployer(Long employerId);
}
