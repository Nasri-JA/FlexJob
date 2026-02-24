package com.flexjob.notification.infrastructure.adapter.input.rest.controller;

import com.flexjob.notification.application.dto.command.CreateNotificationCommand;
import com.flexjob.notification.application.dto.command.MarkAsReadCommand;
import com.flexjob.notification.application.dto.response.NotificationResponse;
import com.flexjob.notification.application.port.input.*;
import com.flexjob.notification.domain.vo.NotificationId;
import com.flexjob.notification.infrastructure.adapter.input.rest.dto.CreateNotificationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/notifications" )
@RequiredArgsConstructor
public class NotificationController
{
   private final CreateNotificationUseCase createNotificationUseCase;
   private final MarkAsReadUseCase markAsReadUseCase;
   private final GetNotificationsUseCase getNotificationsUseCase;
   private final DeleteNotificationUseCase deleteNotificationUseCase;
   private final GetUnreadCountUseCase getUnreadCountUseCase;

   @PostMapping
   public ResponseEntity<NotificationResponse> create(
      @Valid @RequestBody CreateNotificationRequest request )
   {

      CreateNotificationCommand command = CreateNotificationCommand.builder()
                                                                   .userId( request.getUserId() )
                                                                   .type( request.getType() )
                                                                   .title( request.getTitle() )
                                                                   .message( request.getMessage() )
                                                                   .build();

      NotificationResponse response = createNotificationUseCase.createNotification( command );

      return ResponseEntity.status( HttpStatus.CREATED ).body( response );
   }

   @GetMapping( "/{id}" )
   public ResponseEntity<NotificationResponse> getById( @PathVariable Long id )
   {
      NotificationId notificationId = NotificationId.of( id );
      NotificationResponse response = getNotificationsUseCase.getNotificationById( notificationId );
      return ResponseEntity.ok( response );
   }

   @GetMapping( "/user/{userId}" )
   public ResponseEntity<List<NotificationResponse>> getByUser( @PathVariable Long userId )
   {
      List<NotificationResponse> responses = getNotificationsUseCase.getNotificationsForUser( userId );
      return ResponseEntity.ok( responses );
   }

   @GetMapping( "/user/{userId}/unread" )
   public ResponseEntity<List<NotificationResponse>> getUnreadByUser( @PathVariable Long userId )
   {
      List<NotificationResponse> responses = getNotificationsUseCase
         .getUnreadNotificationsForUser( userId );
      return ResponseEntity.ok( responses );
   }

   @GetMapping( "/user/{userId}/unread/count" )
   public ResponseEntity<Long> getUnreadCount( @PathVariable Long userId )
   {
      Long count = getUnreadCountUseCase.getUnreadCount( userId );
      return ResponseEntity.ok( count );
   }

   @PutMapping( "/{id}/read" )
   public ResponseEntity<NotificationResponse> markAsRead(
      @PathVariable Long id,
      @RequestHeader( "X-User-Id" ) Long userId )
   {

      MarkAsReadCommand command = MarkAsReadCommand.builder()
                                                   .notificationId( NotificationId.of( id ) )
                                                   .requestingUserId( userId )
                                                   .build();

      NotificationResponse response = markAsReadUseCase.markAsRead( command );
      return ResponseEntity.ok( response );
   }

   @DeleteMapping( "/{id}" )
   public ResponseEntity<Void> delete(
      @PathVariable Long id,
      @RequestHeader( "X-User-Id" ) Long userId )
   {

      NotificationId notificationId = NotificationId.of( id );
      deleteNotificationUseCase.deleteNotification( notificationId, userId );

      return ResponseEntity.noContent().build();
   }
}
