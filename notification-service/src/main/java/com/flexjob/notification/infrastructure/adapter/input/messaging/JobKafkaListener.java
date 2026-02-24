package com.flexjob.notification.infrastructure.adapter.input.messaging;

import com.flexjob.common.events.JobCreatedEvent;
import com.flexjob.notification.application.dto.command.CreateNotificationCommand;
import com.flexjob.notification.application.port.input.CreateNotificationUseCase;
import com.flexjob.notification.domain.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobKafkaListener
{
   private final CreateNotificationUseCase createNotificationUseCase;

   @KafkaListener( topics = "job-created", groupId = "notification-service" )
   public void handleJobCreated( JobCreatedEvent event )
   {
      log.info( "Received job-created event for job ID: {}", event.getJobId() );

      try
      {
         CreateNotificationCommand command = CreateNotificationCommand.builder()
                                                                      .userId( event.getEmployerId() )
                                                                      .type( NotificationType.JOB_CREATED )
                                                                      .title( "Job Posted Successfully" )
                                                                      .message( String.format(
                                                                         "Your job '%s' has been posted successfully and is now visible to employees. Location: %s, Hourly Rate: $%.2f",
                                                                         event.getTitle(),
                                                                         event.getLocation(),
                                                                         event.getHourlyRate() ) )
                                                                      .build();
         createNotificationUseCase.createNotification( command );

         log.info( "Job creation notification sent to employer: {}", event.getEmployerId() );
      }
      catch ( Exception e )
      {
         log.error( "Error processing job-created event: {}", e.getMessage(), e );
      }
   }
}
