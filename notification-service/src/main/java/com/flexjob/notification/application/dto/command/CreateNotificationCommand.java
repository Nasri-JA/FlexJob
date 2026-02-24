package com.flexjob.notification.application.dto.command;

import com.flexjob.notification.domain.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateNotificationCommand
{
   private final Long userId;
   private final NotificationType type;
   private final String title;
   private final String message;
}
