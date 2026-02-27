package com.flexjob.engagement.application.application.port.input;
import com.flexjob.engagement.application.application.dto.command.CreateApplicationCommand;
import com.flexjob.engagement.application.application.dto.response.ApplicationResponse;
public interface CreateApplicationUseCase {
    ApplicationResponse createApplication(CreateApplicationCommand command);
}
