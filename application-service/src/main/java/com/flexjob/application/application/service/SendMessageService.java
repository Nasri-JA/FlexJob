package com.flexjob.application.application.service;

import com.flexjob.application.application.dto.command.SendMessageCommand;
import com.flexjob.application.application.dto.response.MessageResponse;
import com.flexjob.application.application.port.input.SendMessageUseCase;
import com.flexjob.application.application.port.output.LoadApplicationPort;
import com.flexjob.application.application.port.output.SaveApplicationPort;
import com.flexjob.application.domain.exception.ApplicationNotFoundException;
import com.flexjob.application.domain.model.Application;
import com.flexjob.application.domain.model.ApplicationMessage;
import com.flexjob.application.domain.service.ApplicationDomainService;
import com.flexjob.application.domain.vo.ApplicationId;
import com.flexjob.application.domain.vo.MessageText;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SendMessageService implements SendMessageUseCase {

    private final ApplicationDomainService applicationDomainService;
    private final LoadApplicationPort loadApplicationPort;
    private final SaveApplicationPort saveApplicationPort;

    @Override
    public MessageResponse sendMessage(SendMessageCommand command) {
        log.info("Sending message for application {}", command.getApplicationId());

        ApplicationId id = ApplicationId.of(command.getApplicationId());
        Application application = loadApplicationPort.findById(id)
                .orElseThrow(() -> ApplicationNotFoundException.byId(command.getApplicationId()));

        if (!applicationDomainService.canSendMessage(application, command.getSenderId())) {
            throw new SecurityException("Sender not authorized");
        }

        MessageText messageText = MessageText.of(command.getMessageText());
        ApplicationMessage message = applicationDomainService.createMessage(messageText, command.getSenderId());

        application.addMessage(message);
        saveApplicationPort.save(application);

        return MessageResponse.builder()
                .id(message.getId())
                .messageText(message.getMessageText().getValue())
                .senderId(message.getSenderId())
                .sentAt(message.getSentAt())
                .build();
    }
}
