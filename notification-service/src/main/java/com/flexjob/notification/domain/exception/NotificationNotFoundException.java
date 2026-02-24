package com.flexjob.notification.domain.exception;

import com.flexjob.notification.domain.vo.NotificationId;

public class NotificationNotFoundException extends RuntimeException
{
   public NotificationNotFoundException( String message )
   {
      super( message );
   }

   public static NotificationNotFoundException byId( NotificationId id )
   {
      return new NotificationNotFoundException(
         String.format( "Notification not found with ID: %d", id.getValue() )
      );
   }

   public static NotificationNotFoundException byId( Long id )
   {
      return new NotificationNotFoundException(
         String.format( "Notification not found with ID: %d", id )
      );
   }
}
