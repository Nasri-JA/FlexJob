package com.flexjob.engagement.application.application.service;

import com.flexjob.engagement.application.application.dto.command.UpdateStatusCommand;
import com.flexjob.engagement.application.application.dto.response.ApplicationResponse;
import com.flexjob.engagement.application.application.dto.response.MessageResponse;
import com.flexjob.engagement.application.application.port.input.UpdateApplicationStatusUseCase;
import com.flexjob.engagement.application.application.port.output.LoadApplicationPort;
import com.flexjob.engagement.application.application.port.output.PublishApplicationEventPort;
import com.flexjob.engagement.application.application.port.output.SaveApplicationPort;
import com.flexjob.engagement.application.domain.exception.ApplicationNotFoundException;
import com.flexjob.engagement.application.domain.model.Application;
import com.flexjob.engagement.application.domain.vo.ApplicationId;
import com.flexjob.engagement.application.enums.ApplicationStatus;
import com.flexjob.engagement.booking.application.dto.command.CreateBookingCommand;
import com.flexjob.engagement.booking.application.port.input.CreateBookingUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UpdateApplicationStatusService implements UpdateApplicationStatusUseCase {
    private final LoadApplicationPort loadApplicationPort;
    private final SaveApplicationPort saveApplicationPort;
    private final PublishApplicationEventPort publishEventPort;
    private final CreateBookingUseCase createBookingUseCase;

    @Override
    public ApplicationResponse updateStatus(UpdateStatusCommand command) {
        log.info("Updating application {} to status {}", command.getApplicationId(), command.getNewStatus());
        ApplicationId id = ApplicationId.of(command.getApplicationId());
        Application application = loadApplicationPort.findById(id)
                .orElseThrow(() -> ApplicationNotFoundException.byId(command.getApplicationId()));
        ApplicationStatus oldStatus = application.getStatus();
        application.updateStatus(command.getNewStatus());
        Application saved = saveApplicationPort.save(application);

        if (command.getNewStatus() == ApplicationStatus.ACCEPTED) {
            try {
                CreateBookingCommand bookingCmd = CreateBookingCommand.builder()
                        .jobId(saved.getJobId()).employeeId(saved.getEmployeeId()).employerId(saved.getEmployerId())
                        .applicationId(saved.getId().getValue()).startDate(LocalDate.now().plusDays(1)).endDate(LocalDate.now().plusDays(7)).build();
                createBookingUseCase.createBooking(bookingCmd);
                log.info("Booking created for accepted application {}", saved.getId().getValue());
            } catch (Exception e) { log.error("Failed to create booking for application {}", saved.getId().getValue(), e); }
        }

        try { publishEventPort.publishApplicationStatusChanged(saved, oldStatus); } catch (Exception e) { log.error("Failed to publish event", e); }
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
