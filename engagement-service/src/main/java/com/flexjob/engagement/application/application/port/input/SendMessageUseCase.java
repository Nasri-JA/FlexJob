package com.flexjob.engagement.application.application.port.input;
import com.flexjob.engagement.application.application.dto.command.SendMessageCommand;
import com.flexjob.engagement.application.application.dto.response.MessageResponse;
public interface SendMessageUseCase {
    MessageResponse sendMessage(SendMessageCommand command);
}
