package com.flexjob.notification.domain.model;

import com.flexjob.notification.domain.enums.NotificationType;
import com.flexjob.notification.domain.vo.NotificationId;
import com.flexjob.notification.domain.vo.NotificationMessage;
import com.flexjob.notification.domain.vo.ReadStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Notification
{
   private final NotificationId id;
   private final Long userId;
   private final NotificationType type;
   private final NotificationMessage message;
   private ReadStatus readStatus;
   private final LocalDateTime createdAt;

   public Notification markAsRead()
   {
      if ( readStatus.isRead() )
      {
         throw new IllegalStateException( "Notification is already marked as read" );
      }

      return Notification.builder()
                         .id( this.id )
                         .userId( this.userId )
                         .type( this.type )
                         .message( this.message )
                         .readStatus( ReadStatus.read() )
                         .createdAt( this.createdAt )
                         .build();
   }

   public boolean isRead()
   {
      return readStatus.isRead();
   }

   public boolean isUnread()
   {
      return readStatus.isUnread();
   }
}
