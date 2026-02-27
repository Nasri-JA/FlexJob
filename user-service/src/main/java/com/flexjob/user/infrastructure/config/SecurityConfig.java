package com.flexjob.user.infrastructure.config;

import com.flexjob.user.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig
{

   private final JwtTokenProvider jwtTokenProvider;

   @Bean
   public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception
   {
      http
         .cors( cors -> cors.configurationSource( corsConfigurationSource() ) )
         .csrf( csrf -> csrf.disable() )
         .authorizeHttpRequests( auth -> auth
            .requestMatchers( "/api/v1/auth/**" ).permitAll()
            .requestMatchers( "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html" ).permitAll()
            .requestMatchers( "/actuator/**" ).permitAll()
            .anyRequest().authenticated()
         )
         .sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
         .addFilterBefore( jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class );

      return http.build();
   }

   @Bean
   public JwtAuthenticationFilter jwtAuthenticationFilter()
   {
      return new JwtAuthenticationFilter( jwtTokenProvider );
   }

   @Bean
   public PasswordEncoder passwordEncoder()
   {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource()
   {
      CorsConfiguration configuration = new CorsConfiguration();

      configuration.setAllowedOrigins( Arrays.asList(
         "http://localhost:3000",
         "http://localhost:4200",
         "http://localhost:8080"
      ) );

      configuration.setAllowedMethods( Arrays.asList(
         "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
      ) );

      configuration.setAllowedHeaders( Arrays.asList(
         "Authorization",
         "Content-Type",
         "X-Requested-With"
      ) );

      configuration.setAllowCredentials( true );
      configuration.setMaxAge( 3600L );

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration( "/**", configuration );
      return source;
   }
}
