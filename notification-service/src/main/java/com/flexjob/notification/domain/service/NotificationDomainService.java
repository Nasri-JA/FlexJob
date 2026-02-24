package com.flexjob.notification.domain.service;

import com.flexjob.notification.domain.enums.NotificationType;
import com.flexjob.notification.domain.model.Notification;
import com.flexjob.notification.domain.vo.NotificationMessage;
import com.flexjob.notification.domain.vo.ReadStatus;

import java.time.LocalDateTime;

public class NotificationDomainService
{
   public Notification createNotification(
      Long userId,
      NotificationType type,
      String title,
      String message )
   {
      if ( userId == null || userId <= 0 )
      {
         throw new IllegalArgumentException( "User ID must be positive, got: " + userId );
      }
      if ( type == null )
      {
         throw new IllegalArgumentException( "Notification type cannot be null" );
      }
      NotificationMessage notificationMessage = NotificationMessage.of( title, message );
      ReadStatus readStatus = ReadStatus.unread();
      return Notification.builder()
                         .id( null )
                         .userId( userId )
                         .type( type )
                         .message( notificationMessage )
                         .readStatus( readStatus )
                         .createdAt( LocalDateTime.now() )
                         .build();
   }

   public boolean validateDeletePermission( Notification notification, Long requestingUserId )
   {
      if ( !notification.getUserId().equals( requestingUserId ) )
      {
         throw new IllegalArgumentException(
            String.format( "User %d is not allowed to delete notification belonging to user %d",
                           requestingUserId, notification.getUserId() )
         );
      }
      return true;
   }

   public boolean validateMarkAsReadPermission( Notification notification, Long requestingUserId )
   {
      if ( !notification.getUserId().equals( requestingUserId ) )
      {
         throw new IllegalArgumentException(
            String.format( "User %d is not allowed to mark notification belonging to user %d as read",
                           requestingUserId, notification.getUserId() )
         );
      }
      return true;
   }
}
