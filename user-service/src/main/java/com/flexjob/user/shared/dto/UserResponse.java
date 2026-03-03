package com.flexjob.user.shared.dto;

import com.flexjob.user.domain.model.UserType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse
{
   private String id;
   private String email;
   private UserType userType;
   private boolean active;
   private boolean emailVerified;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private ProfileResponse profile;
}
