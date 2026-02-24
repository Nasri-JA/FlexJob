package com.flexjob.application.infrastructure.adapter.input.rest.dto;

import com.flexjob.application.enums.ApplicationStatus;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    private ApplicationStatus status;
}
