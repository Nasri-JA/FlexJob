package com.flexjob.booking.infrastructure.config;

import com.flexjob.booking.domain.service.BookingDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public BookingDomainService bookingDomainService() {
        return new BookingDomainService();
    }
}
