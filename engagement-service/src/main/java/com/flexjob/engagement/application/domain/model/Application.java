package com.flexjob.engagement.application.domain.model;

import com.flexjob.engagement.application.domain.vo.ApplicationId;
import com.flexjob.engagement.application.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class Application {
    private ApplicationId id;
    private Long jobId;
    private Long employeeId;
    private Long employerId;
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING;
    @Builder.Default
    private List<ApplicationMessage> messages = new ArrayList<>();
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    public List<ApplicationMessage> getMessages() { return Collections.unmodifiableList(messages); }

    public void addMessage(ApplicationMessage message) {
        if (message == null) throw new IllegalArgumentException("Message cannot be null");
        messages.add(message);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(ApplicationStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("Status cannot be null");
        if (status == ApplicationStatus.WITHDRAWN) throw new IllegalStateException("Cannot update status of withdrawn application");
        if (status == newStatus) throw new IllegalStateException("Application is already in status: " + newStatus);
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void withdraw() {
        if (status == ApplicationStatus.WITHDRAWN) throw new IllegalStateException("Application is already withdrawn");
        if (status == ApplicationStatus.ACCEPTED || status == ApplicationStatus.REJECTED)
            throw new IllegalStateException("Cannot withdraw application with status: " + status);
        this.status = ApplicationStatus.WITHDRAWN;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() { return status == ApplicationStatus.PENDING; }
    public boolean isFinalized() {
        return status == ApplicationStatus.ACCEPTED || status == ApplicationStatus.REJECTED || status == ApplicationStatus.WITHDRAWN;
    }
}
