package com.flexjob.user.domain.model;

import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class User
{
   private UserId id;
   private Email email;
   private String passwordHash;
   private UserType userType;
   private UserProfile profile;
   private boolean active;
   private boolean emailVerified;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   public void activate()
   {
      this.active = true;
      this.updatedAt = LocalDateTime.now();
   }

   public void deactivate()
   {
      this.active = false;
      this.updatedAt = LocalDateTime.now();
   }

   public void verifyEmail()
   {
      this.emailVerified = true;
      this.updatedAt = LocalDateTime.now();
   }

   public void changePassword( String newPasswordHash )
   {
      if ( newPasswordHash == null || newPasswordHash.isEmpty() )
      {
         throw new IllegalArgumentException( "Password hash cannot be empty" );
      }
      this.passwordHash = newPasswordHash;
      this.updatedAt = LocalDateTime.now();
   }

   public void updateProfile( UserProfile profile )
   {
      this.profile = profile;
      this.updatedAt = LocalDateTime.now();
   }
}
