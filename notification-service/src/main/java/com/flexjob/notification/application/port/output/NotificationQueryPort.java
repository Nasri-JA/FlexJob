package com.flexjob.notification.application.port.output;

public interface NotificationQueryPort
{
   Long countUnreadByUserId( Long userId );

   Long countByUserId( Long userId );
}
