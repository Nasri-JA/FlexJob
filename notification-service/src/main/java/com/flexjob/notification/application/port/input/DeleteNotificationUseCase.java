package com.flexjob.notification.application.port.input;

import com.flexjob.notification.domain.vo.NotificationId;

public interface DeleteNotificationUseCase
{
   void deleteNotification( NotificationId notificationId, Long requestingUserId );
}
