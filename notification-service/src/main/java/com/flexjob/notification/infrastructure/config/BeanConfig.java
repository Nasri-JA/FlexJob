package com.flexjob.notification.infrastructure.config;

import com.flexjob.notification.domain.service.NotificationDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig
{
   @Bean
   public NotificationDomainService notificationDomainService()
   {
      return new NotificationDomainService();
   }
}
