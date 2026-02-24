package com.flexjob.application.infrastructure.adapter.input.rest.controller;

import com.flexjob.application.application.dto.command.CreateApplicationCommand;
import com.flexjob.application.application.dto.command.SendMessageCommand;
import com.flexjob.application.application.dto.command.UpdateStatusCommand;
import com.flexjob.application.application.dto.response.ApplicationResponse;
import com.flexjob.application.application.dto.response.MessageResponse;
import com.flexjob.application.application.port.input.*;
import com.flexjob.application.infrastructure.adapter.input.rest.dto.CreateApplicationRequest;
import com.flexjob.application.infrastructure.adapter.input.rest.dto.SendMessageRequest;
import com.flexjob.application.infrastructure.adapter.input.rest.dto.UpdateStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final CreateApplicationUseCase createApplicationUseCase;
    private final UpdateApplicationStatusUseCase updateApplicationStatusUseCase;
    private final GetApplicationUseCase getApplicationUseCase;
    private final SendMessageUseCase sendMessageUseCase;

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @RequestBody CreateApplicationRequest request,
            @RequestHeader("X-User-Id") Long employeeId) {
        CreateApplicationCommand cmd = CreateApplicationCommand.builder()
                .jobId(request.getJobId())
                .employeeId(employeeId)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createApplicationUseCase.createApplication(cmd));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getApplicationUseCase.getApplicationById(id));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getByJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(getApplicationUseCase.getApplicationsForJob(jobId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ApplicationResponse>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(getApplicationUseCase.getApplicationsForEmployee(employeeId));
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<ApplicationResponse>> getByEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(getApplicationUseCase.getApplicationsForEmployer(employerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request,
            @RequestHeader("X-User-Id") Long requesterId) {
        UpdateStatusCommand cmd = UpdateStatusCommand.builder()
                .applicationId(id)
                .newStatus(request.getStatus())
                .requesterId(requesterId)
                .build();
        return ResponseEntity.ok(updateApplicationStatusUseCase.updateStatus(cmd));
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable Long id,
            @RequestBody SendMessageRequest request,
            @RequestHeader("X-User-Id") Long senderId) {
        SendMessageCommand cmd = SendMessageCommand.builder()
                .applicationId(id)
                .messageText(request.getMessageText())
                .senderId(senderId)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sendMessageUseCase.sendMessage(cmd));
    }
}
