package com.flexjob.engagement.application.application.port.input;
import com.flexjob.engagement.application.application.dto.command.UpdateStatusCommand;
import com.flexjob.engagement.application.application.dto.response.ApplicationResponse;
public interface UpdateApplicationStatusUseCase {
    ApplicationResponse updateStatus(UpdateStatusCommand command);
}
