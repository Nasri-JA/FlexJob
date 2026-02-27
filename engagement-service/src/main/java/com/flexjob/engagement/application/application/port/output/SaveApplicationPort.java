package com.flexjob.engagement.application.application.port.output;
import com.flexjob.engagement.application.domain.model.Application;
public interface SaveApplicationPort {
    Application save(Application application);
}
