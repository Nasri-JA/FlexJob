package com.flexjob.notification.application.port.output;

import com.flexjob.notification.domain.model.Notification;
import com.flexjob.notification.domain.vo.NotificationId;

import java.util.List;
import java.util.Optional;

public interface LoadNotificationPort
{
   Optional<Notification> findById( NotificationId id );

   List<Notification> findByUserId( Long userId );

   List<Notification> findUnreadByUserId( Long userId );

   boolean existsById( NotificationId id );
}
