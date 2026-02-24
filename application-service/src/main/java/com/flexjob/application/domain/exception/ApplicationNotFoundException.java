package com.flexjob.application.domain.exception;

import com.flexjob.application.domain.vo.ApplicationId;

public class ApplicationNotFoundException extends RuntimeException {

    private final ApplicationId applicationId;

    private ApplicationNotFoundException(String message, ApplicationId applicationId) {
        super(message);
        this.applicationId = applicationId;
    }

    public static ApplicationNotFoundException byId(ApplicationId applicationId) {
        return new ApplicationNotFoundException(
                "Application not found with ID: " + applicationId.getValue(),
                applicationId
        );
    }

    public static ApplicationNotFoundException byId(Long applicationId) {
        return byId(ApplicationId.of(applicationId));
    }

    public ApplicationId getApplicationId() {
        return applicationId;
    }
}
