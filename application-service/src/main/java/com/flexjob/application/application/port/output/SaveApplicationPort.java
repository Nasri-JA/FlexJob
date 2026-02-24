package com.flexjob.application.application.port.output;

import com.flexjob.application.domain.model.Application;

public interface SaveApplicationPort {
    Application save(Application application);
}
