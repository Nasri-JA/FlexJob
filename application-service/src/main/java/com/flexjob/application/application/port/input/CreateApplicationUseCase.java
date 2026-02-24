package com.flexjob.application.application.port.input;

import com.flexjob.application.application.dto.command.CreateApplicationCommand;
import com.flexjob.application.application.dto.response.ApplicationResponse;

public interface CreateApplicationUseCase {
    ApplicationResponse createApplication(CreateApplicationCommand command);
}
