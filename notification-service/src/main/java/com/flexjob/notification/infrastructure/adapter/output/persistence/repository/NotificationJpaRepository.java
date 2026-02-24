package com.flexjob.notification.infrastructure.adapter.output.persistence.repository;

import com.flexjob.notification.infrastructure.adapter.output.persistence.entity.NotificationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, Long>
{
   List<NotificationJpaEntity> findByUserIdOrderByCreatedAtDesc( Long userId );

   List<NotificationJpaEntity> findByUserIdAndIsReadOrderByCreatedAtDesc( Long userId, Boolean isRead );

   @Query( "SELECT COUNT(n) FROM NotificationJpaEntity n WHERE n.userId = :userId AND n.isRead = false" )
   Long countUnreadByUserId( @Param( "userId" ) Long userId );

   Long countByUserId( Long userId );
}
