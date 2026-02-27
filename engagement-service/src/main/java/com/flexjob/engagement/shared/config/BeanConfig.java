package com.flexjob.engagement.shared.config;

import com.flexjob.engagement.application.domain.service.ApplicationDomainService;
import com.flexjob.engagement.booking.domain.service.BookingDomainService;
import com.flexjob.engagement.review.domain.service.ReviewDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ApplicationDomainService applicationDomainService() {
        return new ApplicationDomainService();
    }

    @Bean
    public BookingDomainService bookingDomainService() {
        return new BookingDomainService();
    }

    @Bean
    public ReviewDomainService reviewDomainService() {
        return new ReviewDomainService();
    }
}
