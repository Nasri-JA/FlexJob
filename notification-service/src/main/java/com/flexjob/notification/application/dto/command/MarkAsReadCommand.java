package com.flexjob.notification.application.dto.command;

import com.flexjob.notification.domain.vo.NotificationId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarkAsReadCommand
{
   private final NotificationId notificationId;
   private final Long requestingUserId;
}
