package com.flexjob.engagement.application.application.port.output;
import com.flexjob.engagement.application.domain.model.Application;
import com.flexjob.engagement.application.enums.ApplicationStatus;
public interface PublishApplicationEventPort {
    void publishApplicationCreated(Application application);
    void publishApplicationStatusChanged(Application application, ApplicationStatus oldStatus);
}
