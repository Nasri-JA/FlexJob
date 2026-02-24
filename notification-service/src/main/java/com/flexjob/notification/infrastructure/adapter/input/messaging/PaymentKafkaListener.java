package com.flexjob.notification.infrastructure.adapter.input.messaging;

import com.flexjob.common.events.PaymentCompletedEvent;
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
public class PaymentKafkaListener
{
   private final CreateNotificationUseCase createNotificationUseCase;

   @KafkaListener( topics = "payment-completed", groupId = "notification-service" )
   public void handlePaymentCompleted( PaymentCompletedEvent event )
   {
      log.info( "Received payment-completed event for payment ID: {}", event.getPaymentId() );

      try
      {
         CreateNotificationCommand employeeCommand = CreateNotificationCommand.builder()
                                                                              .userId( event.getEmployeeId() )
                                                                              .type( NotificationType.PAYMENT_COMPLETED )
                                                                              .title( "Payment Received" )
                                                                              .message( String.format(
                                                                                 "Payment of $%.2f for booking ID %d has been successfully processed. "
                                                                                    +
                                                                                    "The amount will be transferred to your account within 2-3 business days.",
                                                                                 event.getAmount(),
                                                                                 event.getBookingId() ) )
                                                                              .build();

         createNotificationUseCase.createNotification( employeeCommand );
         CreateNotificationCommand employerCommand = CreateNotificationCommand.builder()
                                                                              .userId( event.getEmployerId() )
                                                                              .type( NotificationType.PAYMENT_COMPLETED )
                                                                              .title( "Payment Processed" )
                                                                              .message( String.format(
                                                                                 "Your payment of $%.2f for booking ID %d has been successfully processed. "
                                                                                    +
                                                                                    "Thank you for using FlexJob!",
                                                                                 event.getAmount(),
                                                                                 event.getBookingId() ) )
                                                                              .build();

         createNotificationUseCase.createNotification( employerCommand );

         log.info( "Payment completed notifications sent to employee: {} and employer: {}",
                   event.getEmployeeId(), event.getEmployerId() );
      }
      catch ( Exception e )
      {
         log.error( "Error processing payment-completed event: {}", e.getMessage(), e );
      }
   }
}
