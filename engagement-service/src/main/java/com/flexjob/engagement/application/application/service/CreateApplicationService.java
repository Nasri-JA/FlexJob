package com.flexjob.engagement.application.application.service;

import com.flexjob.engagement.application.application.dto.command.CreateApplicationCommand;
import com.flexjob.engagement.application.application.dto.response.ApplicationResponse;
import com.flexjob.engagement.application.application.dto.response.MessageResponse;
import com.flexjob.engagement.application.application.port.input.CreateApplicationUseCase;
import com.flexjob.engagement.application.application.port.output.JobServicePort;
import com.flexjob.engagement.application.application.port.output.LoadApplicationPort;
import com.flexjob.engagement.application.application.port.output.PublishApplicationEventPort;
import com.flexjob.engagement.application.application.port.output.SaveApplicationPort;
import com.flexjob.engagement.application.domain.model.Application;
import com.flexjob.engagement.application.domain.service.ApplicationDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreateApplicationService implements CreateApplicationUseCase {
    private final ApplicationDomainService applicationDomainService;
    private final LoadApplicationPort loadApplicationPort;
    private final SaveApplicationPort saveApplicationPort;
    private final JobServicePort jobServicePort;
    private final PublishApplicationEventPort publishEventPort;

    @Override
    public ApplicationResponse createApplication(CreateApplicationCommand command) {
        log.info("Creating application for job {} by employee {}", command.getJobId(), command.getEmployeeId());
        JobServicePort.JobInfo job = jobServicePort.getJobById(command.getJobId());
        if (job == null) throw new IllegalArgumentException("Job not found: " + command.getJobId());
        if (loadApplicationPort.existsByJobIdAndEmployeeId(command.getJobId(), command.getEmployeeId()))
            throw new IllegalStateException("Employee already applied to this job");
        Application application = applicationDomainService.createApplication(command.getJobId(), command.getEmployeeId(), job.employerId());
        Application saved = saveApplicationPort.save(application);
        try { publishEventPort.publishApplicationCreated(saved); } catch (Exception e) { log.error("Failed to publish event", e); }
        return mapToResponse(saved);
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
