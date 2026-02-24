package com.flexjob.application.infrastructure.config;

import com.flexjob.application.domain.service.ApplicationDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ApplicationDomainService applicationDomainService() {
        return new ApplicationDomainService();
    }
}
