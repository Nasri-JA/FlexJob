package com.flexjob.application.application.port.input;

import com.flexjob.application.application.dto.command.UpdateStatusCommand;
import com.flexjob.application.application.dto.response.ApplicationResponse;

public interface UpdateApplicationStatusUseCase {
    ApplicationResponse updateStatus(UpdateStatusCommand command);
}
