package com.flexjob.notification.application.dto.response;

import com.flexjob.notification.domain.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse
{
   private final Long id;
   private final Long userId;
   private final NotificationType type;
   private final String title;
   private final String message;
   private final Boolean isRead;
   private final LocalDateTime createdAt;
}
