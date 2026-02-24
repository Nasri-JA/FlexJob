package com.flexjob.notification.infrastructure.adapter.input.rest.dto;

import com.flexjob.notification.domain.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateNotificationRequest
{
   @NotNull( message = "User ID is required" )
   private Long userId;

   @NotNull( message = "Notification type is required" )
   private NotificationType type;

   @NotBlank( message = "Title is required" )
   private String title;

   @NotBlank( message = "Message is required" )
   private String message;
}
