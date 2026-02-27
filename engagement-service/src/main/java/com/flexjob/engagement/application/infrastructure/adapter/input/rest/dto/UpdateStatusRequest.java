package com.flexjob.engagement.application.infrastructure.adapter.input.rest.dto;
import com.flexjob.engagement.application.enums.ApplicationStatus;
import lombok.Data;
@Data
public class UpdateStatusRequest { private ApplicationStatus status; }
