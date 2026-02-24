package com.flexjob.notification.application.service;

import com.flexjob.notification.application.dto.response.NotificationResponse;
import com.flexjob.notification.application.port.input.GetNotificationsUseCase;
import com.flexjob.notification.application.port.output.LoadNotificationPort;
import com.flexjob.notification.domain.exception.NotificationNotFoundException;
import com.flexjob.notification.domain.model.Notification;
import com.flexjob.notification.domain.vo.NotificationId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetNotificationsService implements GetNotificationsUseCase
{
   private final LoadNotificationPort loadNotificationPort;

   @Override
   @Transactional( readOnly = true )
   public NotificationResponse getNotificationById( NotificationId id )
   {
      log.info( "Loading notification by ID: {}", id.getValue() );

      Notification notification = loadNotificationPort.findById( id )
                                                      .orElseThrow( () -> NotificationNotFoundException.byId(
                                                         id ) );

      return mapToResponse( notification );
   }

   @Override
   @Transactional( readOnly = true )
   public List<NotificationResponse> getNotificationsForUser( Long userId )
   {
      log.info( "Loading notifications for user: {}", userId );

      List<Notification> notifications = loadNotificationPort.findByUserId( userId );

      return notifications.stream()
                          .map( this :: mapToResponse )
                          .collect( Collectors.toList() );
   }

   @Override
   @Transactional( readOnly = true )
   public List<NotificationResponse> getUnreadNotificationsForUser( Long userId )
   {
      log.info( "Loading unread notifications for user: {}", userId );

      List<Notification> notifications = loadNotificationPort.findUnreadByUserId( userId );

      return notifications.stream()
                          .map( this :: mapToResponse )
                          .collect( Collectors.toList() );
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
