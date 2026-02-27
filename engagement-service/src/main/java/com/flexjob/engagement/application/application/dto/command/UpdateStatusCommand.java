package com.flexjob.engagement.application.application.dto.command;
import com.flexjob.engagement.application.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Value;
@Value
@Builder
public class UpdateStatusCommand {
    Long applicationId;
    ApplicationStatus newStatus;
    Long requesterId;
}
