package com.flexjob.notification.application.port.input;

import com.flexjob.notification.application.dto.command.MarkAsReadCommand;
import com.flexjob.notification.application.dto.response.NotificationResponse;

public interface MarkAsReadUseCase
{
   NotificationResponse markAsRead( MarkAsReadCommand command );
}
