package com.flexjob.notification.infrastructure.adapter.input.messaging;

import com.flexjob.common.events.ApplicationCreatedEvent;
import com.flexjob.common.events.ApplicationStatusChangedEvent;
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
public class ApplicationKafkaListener
{
   private final CreateNotificationUseCase createNotificationUseCase;

   @KafkaListener( topics = "application-created", groupId = "notification-service" )
   public void handleApplicationCreated( ApplicationCreatedEvent event )
   {
      log.info( "Received application-created event for application ID: {}", event.getApplicationId() );

      try
      {
         CreateNotificationCommand command = CreateNotificationCommand.builder()
                                                                      .userId( event.getEmployerId() )
                                                                      .type( NotificationType.APPLICATION_RECEIVED )
                                                                      .title( "New Job Application Received" )
                                                                      .message( String.format(
                                                                         "You have received a new application for job ID %d. Please review the applicant's profile.",
                                                                         event.getJobId() ) )
                                                                      .build();

         createNotificationUseCase.createNotification( command );

         log.info( "Application received notification sent to employer: {}", event.getEmployerId() );
      }
      catch ( Exception e )
      {
         log.error( "Error processing application-created event: {}", e.getMessage(), e );
      }
   }

   @KafkaListener( topics = "application-status-changed", groupId = "notification-service" )
   public void handleApplicationStatusChanged( ApplicationStatusChangedEvent event )
   {
      log.info( "Received application-status-changed event for application ID: {} - Status: {} -> {}",
                event.getApplicationId(), event.getOldStatus(), event.getNewStatus() );

      try
      {
         NotificationType notificationType;
         String title;
         String message;
         switch ( event.getNewStatus().toUpperCase() )
         {
            case "ACCEPTED":
               notificationType = NotificationType.APPLICATION_ACCEPTED;
               title = "Application Accepted";
               message = String.format(
                  "Congratulations! Your application for job ID %d has been accepted. " +
                     "The employer will contact you soon to schedule the work.",
                  event.getJobId() );
               break;

            case "REJECTED":
               notificationType = NotificationType.APPLICATION_REJECTED;
               title = "Application Status Update";
               message = String.format(
                  "Your application for job ID %d has been reviewed. " +
                     "Unfortunately, the employer has decided to proceed with other candidates.",
                  event.getJobId() );
               break;

            default:
               log.info( "No notification required for status: {}", event.getNewStatus() );
               return;
         }
         CreateNotificationCommand command = CreateNotificationCommand.builder()
                                                                      .userId( event.getEmployeeId() )
                                                                      .type( notificationType )
                                                                      .title( title )
                                                                      .message( message )
                                                                      .build();

         createNotificationUseCase.createNotification( command );

         log.info( "Application status notification sent to employee: {}", event.getEmployeeId() );
      }
      catch ( Exception e )
      {
         log.error( "Error processing application-status-changed event: {}", e.getMessage(), e );
      }
   }
}
