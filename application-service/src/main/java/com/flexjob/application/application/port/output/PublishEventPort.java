package com.flexjob.application.application.port.output;

import com.flexjob.application.domain.model.Application;
import com.flexjob.application.enums.ApplicationStatus;

public interface PublishEventPort {
    void publishApplicationCreated(Application application);
    void publishApplicationStatusChanged(Application application, ApplicationStatus oldStatus);
}
