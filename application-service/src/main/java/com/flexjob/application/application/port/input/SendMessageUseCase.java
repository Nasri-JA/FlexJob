package com.flexjob.application.application.port.input;

import com.flexjob.application.application.dto.command.SendMessageCommand;
import com.flexjob.application.application.dto.response.MessageResponse;

public interface SendMessageUseCase {
    MessageResponse sendMessage(SendMessageCommand command);
}
