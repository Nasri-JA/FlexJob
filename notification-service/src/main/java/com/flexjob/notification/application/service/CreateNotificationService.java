package com.flexjob.notification.application.service;

import com.flexjob.notification.application.dto.command.CreateNotificationCommand;
import com.flexjob.notification.application.dto.response.NotificationResponse;
import com.flexjob.notification.application.port.input.CreateNotificationUseCase;
import com.flexjob.notification.application.port.output.SaveNotificationPort;
import com.flexjob.notification.domain.model.Notification;
import com.flexjob.notification.domain.service.NotificationDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateNotificationService implements CreateNotificationUseCase
{
   private final NotificationDomainService notificationDomainService;
   private final SaveNotificationPort saveNotificationPort;

   @Override
   @Transactional
   public NotificationResponse createNotification( CreateNotificationCommand command )
   {
      log.info( "Creating notification for user: {} with type: {}",
                command.getUserId(), command.getType() );
      Notification notification = notificationDomainService.createNotification(
         command.getUserId(),
         command.getType(),
         command.getTitle(),
         command.getMessage()
      );
      Notification saved = saveNotificationPort.save( notification );
      log.info( "Notification created with ID: {}", saved.getId().getValue() );
      return mapToResponse( saved );
   }

   private NotificationResponse mapToResponse( Notification notification )
   {
      return NotificationResponse.builder()
                                 .id( notification.getId().getValue() )
                                 .userId( notification.getUserId() )
                                 .type( notification.getType() )
                                 .title( notification.getMessage().getTitle() )
                                 .message( notification.getMessage().getMessage() )
                                 .isRead( notification.getReadStatus().isRead() )
                                 .createdAt( notification.getCreatedAt() )
                                 .build();
   }
}
