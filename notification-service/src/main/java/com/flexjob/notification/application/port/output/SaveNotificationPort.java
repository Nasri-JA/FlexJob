package com.flexjob.notification.application.port.output;

import com.flexjob.notification.domain.model.Notification;

public interface SaveNotificationPort
{
   Notification save( Notification notification );
}
