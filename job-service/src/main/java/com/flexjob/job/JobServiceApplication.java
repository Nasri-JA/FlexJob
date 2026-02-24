package com.flexjob.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication( scanBasePackages = { "com.flexjob.job", "com.flexjob.common" } )
@EnableDiscoveryClient
public class JobServiceApplication
{
   public static void main( String[] args )
   {
      SpringApplication.run( JobServiceApplication.class, args );
   }
}
