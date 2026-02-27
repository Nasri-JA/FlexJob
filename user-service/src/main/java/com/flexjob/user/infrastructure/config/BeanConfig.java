package com.flexjob.user.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flexjob.user.domain.service.UserDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig
{
   @Bean
   public ObjectMapper objectMapper()
   {
      ObjectMapper mapper = new ObjectMapper();

      mapper.registerModule( new JavaTimeModule() );

      mapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );

      return mapper;
   }

   @Bean
   public UserDomainService userDomainService()
   {
      return new UserDomainService();
   }

}
