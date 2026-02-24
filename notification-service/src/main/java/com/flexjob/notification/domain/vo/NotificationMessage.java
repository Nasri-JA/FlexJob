package com.flexjob.notification.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class NotificationMessage
{
   private static final int MAX_TITLE_LENGTH = 200;
   private static final int MAX_MESSAGE_LENGTH = 1000;

   private final String title;
   private final String message;

   private NotificationMessage( String title, String message )
   {
      this.title = validateTitle( title );
      this.message = validateMessage( message );
   }

   public static NotificationMessage of( String title, String message )
   {
      return new NotificationMessage( title, message );
   }

   private String validateTitle( String title )
   {
      if ( title == null || title.trim().isEmpty() )
      {
         throw new IllegalArgumentException( "Notification title cannot be empty" );
      }

      if ( title.length() > MAX_TITLE_LENGTH )
      {
         throw new IllegalArgumentException(
            String.format( "Notification title cannot exceed %d characters, got: %d",
                           MAX_TITLE_LENGTH, title.length() )
         );
      }

      return title.trim();
   }

   private String validateMessage( String message )
   {
      if ( message == null || message.trim().isEmpty() )
      {
         throw new IllegalArgumentException( "Notification message cannot be empty" );
      }

      if ( message.length() > MAX_MESSAGE_LENGTH )
      {
         throw new IllegalArgumentException(
            String.format( "Notification message cannot exceed %d characters, got: %d",
                           MAX_MESSAGE_LENGTH, message.length() )
         );
      }

      return message.trim();
   }

   @Override
   public String toString()
   {
      return String.format( "NotificationMessage(title='%s', message='%s')", title, message );
   }
}
