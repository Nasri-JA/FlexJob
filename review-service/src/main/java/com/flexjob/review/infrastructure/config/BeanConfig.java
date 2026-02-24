package com.flexjob.review.infrastructure.config;

import com.flexjob.review.domain.service.ReviewDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public ReviewDomainService reviewDomainService() {
        return new ReviewDomainService();
    }
}
