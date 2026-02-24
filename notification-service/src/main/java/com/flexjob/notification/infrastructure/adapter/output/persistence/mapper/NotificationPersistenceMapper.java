package com.flexjob.notification.infrastructure.adapter.output.persistence.mapper;

import com.flexjob.notification.domain.model.Notification;
import com.flexjob.notification.domain.vo.NotificationId;
import com.flexjob.notification.domain.vo.NotificationMessage;
import com.flexjob.notification.domain.vo.ReadStatus;
import com.flexjob.notification.infrastructure.adapter.output.persistence.entity.NotificationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationPersistenceMapper
{
   public NotificationJpaEntity toJpaEntity( Notification notification )
   {
      return NotificationJpaEntity.builder()
                                  .id( notification.getId() != null ? notification.getId().getValue() : null )
                                  .userId( notification.getUserId() )
                                  .type( notification.getType() )
                                  .title( notification.getMessage().getTitle() )
                                  .message( notification.getMessage().getMessage() )
                                  .isRead( notification.getReadStatus().isRead() )
                                  .createdAt( notification.getCreatedAt() )
                                  .build();
   }

   public Notification toDomain( NotificationJpaEntity jpaEntity )
   {
      return Notification.builder()
                         .id( NotificationId.of( jpaEntity.getId() ) )
                         .userId( jpaEntity.getUserId() )
                         .type( jpaEntity.getType() )
                         .message( NotificationMessage.of( jpaEntity.getTitle(), jpaEntity.getMessage() ) )
                         .readStatus( ReadStatus.from( jpaEntity.getIsRead() ) )
                         .createdAt( jpaEntity.getCreatedAt() )
                         .build();
   }
}
