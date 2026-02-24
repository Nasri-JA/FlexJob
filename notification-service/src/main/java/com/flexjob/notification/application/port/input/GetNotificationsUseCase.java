package com.flexjob.notification.application.port.input;

import com.flexjob.notification.application.dto.response.NotificationResponse;
import com.flexjob.notification.domain.vo.NotificationId;

import java.util.List;

public interface GetNotificationsUseCase
{
   NotificationResponse getNotificationById( NotificationId id );

   List<NotificationResponse> getNotificationsForUser( Long userId );

   List<NotificationResponse> getUnreadNotificationsForUser( Long userId );
}
