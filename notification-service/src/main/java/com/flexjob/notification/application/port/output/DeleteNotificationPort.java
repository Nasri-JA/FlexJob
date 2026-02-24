package com.flexjob.notification.application.port.output;

import com.flexjob.notification.domain.vo.NotificationId;

public interface DeleteNotificationPort
{
   void deleteById( NotificationId id );
}
