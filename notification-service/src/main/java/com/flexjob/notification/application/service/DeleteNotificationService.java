package com.flexjob.notification.application.service;

import com.flexjob.notification.application.port.input.DeleteNotificationUseCase;
import com.flexjob.notification.application.port.output.DeleteNotificationPort;
import com.flexjob.notification.application.port.output.LoadNotificationPort;
import com.flexjob.notification.domain.exception.NotificationNotFoundException;
import com.flexjob.notification.domain.model.Notification;
import com.flexjob.notification.domain.service.NotificationDomainService;
import com.flexjob.notification.domain.vo.NotificationId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteNotificationService implements DeleteNotificationUseCase
{
   private final NotificationDomainService notificationDomainService;
   private final LoadNotificationPort loadNotificationPort;
   private final DeleteNotificationPort deleteNotificationPort;

   @Override
   @Transactional
   public void deleteNotification( NotificationId notificationId, Long requestingUserId )
   {
      log.info( "Deleting notification: {} by user: {}", notificationId.getValue(), requestingUserId );

      Notification notification = loadNotificationPort.findById( notificationId )
                                                      .orElseThrow( () -> NotificationNotFoundException.byId(
                                                         notificationId ) );
      notificationDomainService.validateDeletePermission( notification, requestingUserId );
      deleteNotificationPort.deleteById( notificationId );
      log.info( "Notification deleted: {}", notificationId.getValue() );
   }
}
