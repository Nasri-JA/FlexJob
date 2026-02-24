package com.flexjob.user.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
   info = @Info(
      title = "User Service API",
      version = "1.0",
      description = "API for user management, authentication and profiles"
   )
)
@SecurityScheme(
   name = "bearerAuth",
   type = SecuritySchemeType.HTTP,
   scheme = "bearer",
   bearerFormat = "JWT"
)
public class OpenApiConfig
{
}
