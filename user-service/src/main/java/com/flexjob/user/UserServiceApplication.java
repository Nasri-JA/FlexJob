package com.flexjob.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UserServiceApplication
{
   public static void main( String[] args )
   {
      SpringApplication.run( UserServiceApplication.class, args );

      System.out.println( "=".repeat( 60 ) );
      System.out.println( "  USER-SERVICE ERFOLGREICH GESTARTET" );
      System.out.println( "=".repeat( 60 ) );
      System.out.println( "  REST-API: http://localhost:8080/api/v1" );
      System.out.println( "  Swagger-UI: http://localhost:8080/swagger-ui.html" );
      System.out.println( "  Health: http://localhost:8080/actuator/health" );
      System.out.println( "=".repeat( 60 ) );

   }

}
