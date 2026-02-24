package com.flexjob.notification.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class NotificationId
{
   private final Long value;

   private NotificationId( Long value )
   {
      if ( value == null || value <= 0 )
      {
         throw new IllegalArgumentException( "Notification ID must be positive, got: " + value );
      }
      this.value = value;
   }

   public static NotificationId of( Long value )
   {
      return new NotificationId( value );
   }

   @Override
   public String toString()
   {
      return "NotificationId(" + value + ")";
   }
}
