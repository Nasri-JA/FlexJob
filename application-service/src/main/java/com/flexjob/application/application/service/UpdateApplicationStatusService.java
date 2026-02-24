package com.flexjob.application.application.service;

import com.flexjob.application.application.dto.command.UpdateStatusCommand;
import com.flexjob.application.application.dto.response.ApplicationResponse;
import com.flexjob.application.application.dto.response.MessageResponse;
import com.flexjob.application.application.port.input.UpdateApplicationStatusUseCase;
import com.flexjob.application.application.port.output.LoadApplicationPort;
import com.flexjob.application.application.port.output.PublishEventPort;
import com.flexjob.application.application.port.output.SaveApplicationPort;
import com.flexjob.application.domain.exception.ApplicationNotFoundException;
import com.flexjob.application.domain.model.Application;
import com.flexjob.application.domain.vo.ApplicationId;
import com.flexjob.application.enums.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UpdateApplicationStatusService implements UpdateApplicationStatusUseCase {

    private final LoadApplicationPort loadApplicationPort;
    private final SaveApplicationPort saveApplicationPort;
    private final PublishEventPort publishEventPort;

    @Override
    public ApplicationResponse updateStatus(UpdateStatusCommand command) {
        log.info("Updating application {} to status {}", command.getApplicationId(), command.getNewStatus());

        ApplicationId id = ApplicationId.of(command.getApplicationId());
        Application application = loadApplicationPort.findById(id)
                .orElseThrow(() -> ApplicationNotFoundException.byId(command.getApplicationId()));

        ApplicationStatus oldStatus = application.getStatus();
        application.updateStatus(command.getNewStatus());

        Application saved = saveApplicationPort.save(application);

        try {
            publishEventPort.publishApplicationStatusChanged(saved, oldStatus);
        } catch (Exception e) {
            log.error("Failed to publish event", e);
        }

        return mapToResponse(saved);
    }

    private ApplicationResponse mapToResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId().getValue())
                .jobId(application.getJobId())
                .employeeId(application.getEmployeeId())
                .employerId(application.getEmployerId())
                .status(application.getStatus())
                .messages(application.getMessages().stream()
                        .map(msg -> MessageResponse.builder()
                                .id(msg.getId())
                                .messageText(msg.getMessageText().getValue())
                                .senderId(msg.getSenderId())
                                .sentAt(msg.getSentAt())
                                .build())
                        .collect(Collectors.toList()))
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
