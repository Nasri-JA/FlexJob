package com.flexjob.notification.infrastructure.adapter.input.messaging;

import com.flexjob.common.events.BookingCompletedEvent;
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
public class BookingKafkaListener
{
   private final CreateNotificationUseCase createNotificationUseCase;

   @KafkaListener( topics = "booking-completed", groupId = "notification-service" )
   public void handleBookingCompleted( BookingCompletedEvent event )
   {
      log.info( "Received booking-completed event for booking ID: {}", event.getBookingId() );

      try
      {
         CreateNotificationCommand employeeCommand = CreateNotificationCommand.builder()
                                                                              .userId( event.getEmployeeId() )
                                                                              .type( NotificationType.BOOKING_COMPLETED )
                                                                              .title( "Job Completed" )
                                                                              .message( String.format(
                                                                                 "Congratulations! You have successfully completed the job for booking ID %d. "
                                                                                    +
                                                                                    "Payment will be processed soon.",
                                                                                 event.getBookingId() ) )
                                                                              .build();

         createNotificationUseCase.createNotification( employeeCommand );
         CreateNotificationCommand employerCommand = CreateNotificationCommand.builder()
                                                                              .userId( event.getEmployerId() )
                                                                              .type( NotificationType.BOOKING_COMPLETED )
                                                                              .title( "Job Completed" )
                                                                              .message( String.format(
                                                                                 "The job for booking ID %d has been marked as completed. "
                                                                                    +
                                                                                    "Please review the work and provide feedback.",
                                                                                 event.getBookingId() ) )
                                                                              .build();

         createNotificationUseCase.createNotification( employerCommand );

         log.info( "Booking completed notifications sent to employee: {} and employer: {}",
                   event.getEmployeeId(), event.getEmployerId() );
      }
      catch ( Exception e )
      {
         log.error( "Error processing booking-completed event: {}", e.getMessage(), e );
      }
   }
}
