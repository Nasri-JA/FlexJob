package com.flexjob.notification.infrastructure.adapter.output.persistence.entity;

import com.flexjob.notification.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table( name = "notifications" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationJpaEntity
{
   @Id
   @GeneratedValue( strategy = GenerationType.IDENTITY )
   private Long id;

   @Column( nullable = false )
   private Long userId;

   @Enumerated( EnumType.STRING )
   @Column( nullable = false )
   private NotificationType type;

   @Column( nullable = false, length = 200 )
   private String title;

   @Column( nullable = false, length = 1000 )
   private String message;

   @Column( nullable = false )
   @Builder.Default
   private Boolean isRead = false;

   @CreationTimestamp
   @Column( nullable = false, updatable = false )
   private LocalDateTime createdAt;
}
