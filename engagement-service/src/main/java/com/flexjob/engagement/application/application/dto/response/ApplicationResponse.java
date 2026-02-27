package com.flexjob.engagement.application.application.dto.response;
import com.flexjob.engagement.application.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.List;
@Value
@Builder
public class ApplicationResponse {
    Long id;
    Long jobId;
    Long employeeId;
    Long employerId;
    ApplicationStatus status;
    List<MessageResponse> messages;
    LocalDateTime appliedAt;
    LocalDateTime updatedAt;
}
