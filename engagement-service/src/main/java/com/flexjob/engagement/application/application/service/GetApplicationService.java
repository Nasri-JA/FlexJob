package com.flexjob.engagement.application.application.service;

import com.flexjob.engagement.application.application.dto.response.ApplicationResponse;
import com.flexjob.engagement.application.application.dto.response.MessageResponse;
import com.flexjob.engagement.application.application.port.input.GetApplicationUseCase;
import com.flexjob.engagement.application.application.port.output.LoadApplicationPort;
import com.flexjob.engagement.application.domain.exception.ApplicationNotFoundException;
import com.flexjob.engagement.application.domain.model.Application;
import com.flexjob.engagement.application.domain.vo.ApplicationId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GetApplicationService implements GetApplicationUseCase {
    private final LoadApplicationPort loadApplicationPort;

    @Override
    public ApplicationResponse getApplicationById(Long applicationId) {
        ApplicationId id = ApplicationId.of(applicationId);
        Application application = loadApplicationPort.findById(id).orElseThrow(() -> ApplicationNotFoundException.byId(applicationId));
        return mapToResponse(application);
    }
    @Override
    public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
        return loadApplicationPort.findByJobId(jobId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Override
    public List<ApplicationResponse> getApplicationsForEmployee(Long employeeId) {
        return loadApplicationPort.findByEmployeeId(employeeId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Override
    public List<ApplicationResponse> getApplicationsForEmployer(Long employerId) {
        return loadApplicationPort.findByEmployerId(employerId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private ApplicationResponse mapToResponse(Application application) {
        return ApplicationResponse.builder().id(application.getId().getValue()).jobId(application.getJobId())
                .employeeId(application.getEmployeeId()).employerId(application.getEmployerId()).status(application.getStatus())
                .messages(application.getMessages().stream().map(msg -> MessageResponse.builder().id(msg.getId())
                        .messageText(msg.getMessageText().getValue()).senderId(msg.getSenderId()).sentAt(msg.getSentAt()).build())
                        .collect(Collectors.toList()))
                .appliedAt(application.getAppliedAt()).updatedAt(application.getUpdatedAt()).build();
    }
}
