package com.flexjob.application.application.dto.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateApplicationCommand {
    Long jobId;
    Long employeeId;
}
