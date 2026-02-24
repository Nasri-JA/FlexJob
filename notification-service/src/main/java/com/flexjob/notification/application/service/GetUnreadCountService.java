package com.flexjob.notification.application.service;

import com.flexjob.notification.application.port.input.GetUnreadCountUseCase;
import com.flexjob.notification.application.port.output.NotificationQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetUnreadCountService implements GetUnreadCountUseCase
{
   private final NotificationQueryPort notificationQueryPort;

   @Override
   @Transactional( readOnly = true )
   public Long getUnreadCount( Long userId )
   {
      log.info( "Getting unread count for user: {}", userId );
      Long count = notificationQueryPort.countUnreadByUserId( userId );

      log.info( "User {} has {} unread notifications", userId, count );
      return count;
   }
}
