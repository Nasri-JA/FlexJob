package com.flexjob.notification.application.service;

import com.flexjob.notification.application.dto.command.MarkAsReadCommand;
import com.flexjob.notification.application.dto.response.NotificationResponse;
import com.flexjob.notification.application.port.input.MarkAsReadUseCase;
import com.flexjob.notification.application.port.output.LoadNotificationPort;
import com.flexjob.notification.application.port.output.SaveNotificationPort;
import com.flexjob.notification.domain.exception.NotificationNotFoundException;
import com.flexjob.notification.domain.model.Notification;
import com.flexjob.notification.domain.service.NotificationDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarkAsReadService implements MarkAsReadUseCase
{
   private final NotificationDomainService notificationDomainService;
   private final LoadNotificationPort loadNotificationPort;
   private final SaveNotificationPort saveNotificationPort;

   @Override
   @Transactional
   public NotificationResponse markAsRead( MarkAsReadCommand command )
   {
      log.info( "Marking notification as read: {} by user: {}",
                command.getNotificationId().getValue(), command.getRequestingUserId() );

      Notification notification = loadNotificationPort.findById( command.getNotificationId() )
                                                      .orElseThrow( () -> NotificationNotFoundException.byId(
                                                         command.getNotificationId() ) );
      notificationDomainService.validateMarkAsReadPermission(
         notification,
         command.getRequestingUserId()
      );
      Notification updatedNotification = notification.markAsRead();
      Notification saved = saveNotificationPort.save( updatedNotification );
      log.info( "Notification marked as read: {}", saved.getId().getValue() );
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
