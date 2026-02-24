package com.flexjob.application.infrastructure.adapter.output.persistence.mapper;

import com.flexjob.application.domain.model.Application;
import com.flexjob.application.domain.model.ApplicationMessage;
import com.flexjob.application.domain.vo.ApplicationId;
import com.flexjob.application.domain.vo.MessageText;
import com.flexjob.application.infrastructure.adapter.output.persistence.entity.ApplicationJpaEntity;
import com.flexjob.application.infrastructure.adapter.output.persistence.entity.ApplicationMessageJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationPersistenceMapper {

    public Application toDomain(ApplicationJpaEntity jpa) {
        if (jpa == null) return null;

        List<ApplicationMessage> messages = jpa.getMessages().stream()
                .map(msgJpa -> ApplicationMessage.builder()
                        .id(msgJpa.getId())
                        .messageText(MessageText.of(msgJpa.getMessageText()))
                        .senderId(msgJpa.getSenderId())
                        .sentAt(msgJpa.getSentAt())
                        .build())
                .toList();

        return Application.builder()
                .id(ApplicationId.of(jpa.getId()))
                .jobId(jpa.getJobId())
                .employeeId(jpa.getEmployeeId())
                .employerId(jpa.getEmployerId())
                .status(jpa.getStatus())
                .messages(new ArrayList<>(messages))
                .appliedAt(jpa.getAppliedAt())
                .updatedAt(jpa.getUpdatedAt())
                .build();
    }

    public ApplicationJpaEntity toJpaEntity(Application domain) {
        if (domain == null) return null;

        Long id = domain.getId() != null ? domain.getId().getValue() : null;

        ApplicationJpaEntity jpa = ApplicationJpaEntity.builder()
                .id(id)
                .jobId(domain.getJobId())
                .employeeId(domain.getEmployeeId())
                .employerId(domain.getEmployerId())
                .status(domain.getStatus())
                .appliedAt(domain.getAppliedAt())
                .updatedAt(domain.getUpdatedAt())
                .messages(new ArrayList<>())
                .build();

        for (ApplicationMessage msg : domain.getMessages()) {
            ApplicationMessageJpaEntity msgJpa = ApplicationMessageJpaEntity.builder()
                    .id(msg.getId())
                    .messageText(msg.getMessageText().getValue())
                    .senderId(msg.getSenderId())
                    .sentAt(msg.getSentAt())
                    .build();
            jpa.addMessage(msgJpa);
        }

        return jpa;
    }

    public List<Application> toDomainList(List<ApplicationJpaEntity> jpaList) {
        return jpaList.stream().map(this::toDomain).toList();
    }
}
