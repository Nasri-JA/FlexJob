package com.flexjob.notification.application.port.input;

import com.flexjob.notification.application.dto.command.CreateNotificationCommand;
import com.flexjob.notification.application.dto.response.NotificationResponse;

public interface CreateNotificationUseCase
{
   NotificationResponse createNotification( CreateNotificationCommand command );
}
