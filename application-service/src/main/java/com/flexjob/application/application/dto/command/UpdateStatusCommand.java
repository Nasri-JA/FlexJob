package com.flexjob.application.application.dto.command;

import com.flexjob.application.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateStatusCommand {
    Long applicationId;
    ApplicationStatus newStatus;
    Long requesterId; // Employer or Employee
}
