package com.flexjob.payment.infrastructure.config;

import com.flexjob.payment.domain.service.PaymentDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainService();
    }
}
