package com.flexjob.application.infrastructure.adapter.output.messaging;

import com.flexjob.application.application.port.output.PublishEventPort;
import com.flexjob.application.domain.model.Application;
import com.flexjob.application.enums.ApplicationStatus;
import com.flexjob.common.events.ApplicationCreatedEvent;
import com.flexjob.common.events.ApplicationStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationEventPublisher implements PublishEventPort {

    private final KafkaTemplate<String, ApplicationCreatedEvent> createdKafkaTemplate;
    private final KafkaTemplate<String, ApplicationStatusChangedEvent> statusChangedKafkaTemplate;

    @Override
    public void publishApplicationCreated(Application application) {
        try {
            ApplicationCreatedEvent event = ApplicationCreatedEvent.builder()
                    .applicationId(application.getId().getValue())
                    .jobId(application.getJobId())
                    .employeeId(application.getEmployeeId())
                    .employerId(application.getEmployerId())
                    .appliedAt(application.getAppliedAt())
                    .build();
            createdKafkaTemplate.send("application-created", event);
            log.info("Published ApplicationCreatedEvent for {}", application.getId().getValue());
        } catch (Exception e) {
            log.error("Failed to publish event", e);
        }
    }

    @Override
    public void publishApplicationStatusChanged(Application application, ApplicationStatus oldStatus) {
        try {
            ApplicationStatusChangedEvent event = ApplicationStatusChangedEvent.builder()
                    .applicationId(application.getId().getValue())
                    .jobId(application.getJobId())
                    .employeeId(application.getEmployeeId())
                    .employerId(application.getEmployerId())
                    .oldStatus(oldStatus.name())
                    .newStatus(application.getStatus().name())
                    .changedAt(LocalDateTime.now())
                    .build();
            statusChangedKafkaTemplate.send("application-status-changed", event);
            log.info("Published StatusChangedEvent for {}", application.getId().getValue());
        } catch (Exception e) {
            log.error("Failed to publish event", e);
        }
    }
}
