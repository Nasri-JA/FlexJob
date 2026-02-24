package com.flexjob.notification.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ReadStatus
{
   private final boolean read;

   private ReadStatus( boolean read )
   {
      this.read = read;
   }

   public static ReadStatus read()
   {
      return new ReadStatus( true );
   }

   public static ReadStatus unread()
   {
      return new ReadStatus( false );
   }

   public static ReadStatus from( boolean read )
   {
      return read ? read() : unread();
   }

   public ReadStatus markAsRead()
   {
      return read();
   }

   public boolean isRead()
   {
      return read;
   }

   public boolean isUnread()
   {
      return !read;
   }

   @Override
   public String toString()
   {
      return read ? "READ" : "UNREAD";
   }
}
