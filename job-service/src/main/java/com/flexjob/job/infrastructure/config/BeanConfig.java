package com.flexjob.job.infrastructure.config;

import com.flexjob.job.domain.service.JobDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig
{
   @Bean
   public JobDomainService jobDomainService()
   {
      return new JobDomainService();
   }
}
