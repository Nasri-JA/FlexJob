package com.flexjob.notification.infrastructure.adapter.output.persistence.adapter;

import com.flexjob.notification.application.port.output.DeleteNotificationPort;
import com.flexjob.notification.application.port.output.LoadNotificationPort;
import com.flexjob.notification.application.port.output.NotificationQueryPort;
import com.flexjob.notification.application.port.output.SaveNotificationPort;
import com.flexjob.notification.domain.model.Notification;
import com.flexjob.notification.domain.vo.NotificationId;
import com.flexjob.notification.infrastructure.adapter.output.persistence.entity.NotificationJpaEntity;
import com.flexjob.notification.infrastructure.adapter.output.persistence.mapper.NotificationPersistenceMapper;
import com.flexjob.notification.infrastructure.adapter.output.persistence.repository.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationPersistenceAdapter implements
   LoadNotificationPort,
   SaveNotificationPort,
   DeleteNotificationPort,
   NotificationQueryPort
{
   private final NotificationJpaRepository repository;
   private final NotificationPersistenceMapper mapper;

   @Override
   public Optional<Notification> findById( NotificationId id )
   {
      log.debug( "Loading notification by ID: {}", id.getValue() );

      return repository.findById( id.getValue() )
                       .map( mapper :: toDomain );
   }

   @Override
   public List<Notification> findByUserId( Long userId )
   {
      log.debug( "Loading notifications for user: {}", userId );

      List<NotificationJpaEntity> entities = repository.findByUserIdOrderByCreatedAtDesc( userId );

      return entities.stream()
                     .map( mapper :: toDomain )
                     .collect( Collectors.toList() );
   }

   @Override
   public List<Notification> findUnreadByUserId( Long userId )
   {
      log.debug( "Loading unread notifications for user: {}", userId );

      List<NotificationJpaEntity> entities = repository
         .findByUserIdAndIsReadOrderByCreatedAtDesc( userId, false );

      return entities.stream()
                     .map( mapper :: toDomain )
                     .collect( Collectors.toList() );
   }

   @Override
   public boolean existsById( NotificationId id )
   {
      return repository.existsById( id.getValue() );
   }

   @Override
   public Notification save( Notification notification )
   {
      log.debug( "Saving notification: {}",
                 notification.getId() != null ? notification.getId().getValue() : "new" );

      NotificationJpaEntity jpaEntity = mapper.toJpaEntity( notification );
      NotificationJpaEntity saved = repository.save( jpaEntity );

      return mapper.toDomain( saved );
   }

   @Override
   public void deleteById( NotificationId id )
   {
      log.debug( "Deleting notification: {}", id.getValue() );

      repository.deleteById( id.getValue() );
   }

   @Override
   public Long countUnreadByUserId( Long userId )
   {
      log.debug( "Counting unread notifications for user: {}", userId );

      return repository.countUnreadByUserId( userId );
   }

   @Override
   public Long countByUserId( Long userId )
   {
      log.debug( "Counting all notifications for user: {}", userId );

      return repository.countByUserId( userId );
   }
}
