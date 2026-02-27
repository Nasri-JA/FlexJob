package com.flexjob.engagement.application.domain.service;

import com.flexjob.engagement.application.domain.model.Application;
import com.flexjob.engagement.application.domain.model.ApplicationMessage;
import com.flexjob.engagement.application.domain.vo.MessageText;
import com.flexjob.engagement.application.enums.ApplicationStatus;
import java.time.LocalDateTime;

public class ApplicationDomainService {
    public Application createApplication(Long jobId, Long employeeId, Long employerId) {
        if (jobId == null || jobId <= 0) throw new IllegalArgumentException("Invalid job ID");
        if (employeeId == null || employeeId <= 0) throw new IllegalArgumentException("Invalid employee ID");
        if (employerId == null || employerId <= 0) throw new IllegalArgumentException("Invalid employer ID");
        LocalDateTime now = LocalDateTime.now();
        return Application.builder().id(null).jobId(jobId).employeeId(employeeId).employerId(employerId)
                .status(ApplicationStatus.PENDING).appliedAt(now).updatedAt(now).build();
    }
    public ApplicationMessage createMessage(MessageText messageText, Long senderId) { return ApplicationMessage.create(messageText, senderId); }
    public boolean canSendMessage(Application application, Long senderId) {
        return senderId.equals(application.getEmployeeId()) || senderId.equals(application.getEmployerId());
    }
    public boolean isValidStatusTransition(ApplicationStatus from, ApplicationStatus to) {
        if (from == to) return false;
        if (from == ApplicationStatus.WITHDRAWN) return false;
        return switch (from) {
            case PENDING -> to == ApplicationStatus.ACCEPTED || to == ApplicationStatus.REJECTED || to == ApplicationStatus.WITHDRAWN;
            case ACCEPTED, REJECTED, WITHDRAWN -> false;
        };
    }
}
