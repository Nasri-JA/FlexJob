package com.flexjob.user.infrastructure.persistence;

import com.flexjob.user.domain.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "users",
        uniqueConstraints = {
           @UniqueConstraint( columnNames = "email" )
        },
        indexes = {
           @Index( name = "idx_user_email", columnList = "email" ),
           @Index( name = "idx_user_type", columnList = "user_type" )
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJpaEntity
{
   @Id
   @Column( name = "id", nullable = false, length = 36 )
   private String id;

   @Column( name = "email", nullable = false, unique = true, length = 255 )
   private String email;

   @Column( name = "password_hash", nullable = false, length = 60 )
   private String passwordHash;

   @Enumerated( EnumType.STRING )
   @Column( name = "user_type", nullable = false, length = 20 )
   private UserType userType;

   @Column( name = "active", nullable = false )
   private boolean active = true;

   @Column( name = "email_verified", nullable = false )
   private boolean emailVerified = false;

   @Column( name = "created_at", nullable = false, updatable = false )
   private LocalDateTime createdAt;

   @Column( name = "updated_at", nullable = false )
   private LocalDateTime updatedAt;

   @OneToOne( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY )
   @JoinColumn( name = "profile_id", referencedColumnName = "id" )
   private UserProfileJpaEntity profile;

   @Version
   @Column( name = "version" )
   private Long version;

   @PrePersist
   protected void onCreate()
   {
      createdAt = LocalDateTime.now();
      updatedAt = LocalDateTime.now();
   }

   @PreUpdate
   protected void onUpdate()
   {
      updatedAt = LocalDateTime.now();
   }
}
