package com.flexjob.user.application.service;

import com.flexjob.user.application.dto.command.RegisterUserCommand;
import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.application.port.input.RegisterUserUseCase;
import com.flexjob.user.application.port.output.LoadUserPort;
import com.flexjob.user.application.port.output.PublishEventPort;
import com.flexjob.user.application.port.output.SaveUserPort;
import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.model.UserProfile;
import com.flexjob.user.domain.service.UserDomainService;
import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegisterUserService implements RegisterUserUseCase
{
   private final LoadUserPort loadUserPort;
   private final SaveUserPort saveUserPort;
   private final PublishEventPort publishEventPort;
   private final UserDomainService domainService;
   private final PasswordEncoder passwordEncoder;

   @Override
   public UserResponse register( RegisterUserCommand command )
   {
      log.info( "Registering new user with email: {}", command.getEmail() );

      validateCommand( command );

      Email email = Email.of( command.getEmail() );
      log.debug( "Email validated and normalized: {}", email.getValue() );

      if ( loadUserPort.existsByEmail( email ) )
      {
         log.warn( "Registration attempt with existing email: {}", email.getMasked() );
         throw new IllegalArgumentException(
            "E-Mail bereits registriert: " + email.getValue()
         );
      }

      domainService.validateRegistration( email, command.getPassword() );
      log.debug( "Domain validation passed" );

      String passwordHash = passwordEncoder.encode( command.getPassword() );
      log.debug( "Password hashed with BCrypt" );

      User user = User.builder()
                      .id( UserId.generate() )
                      .email( email )
                      .passwordHash( passwordHash )
                      .userType( command.getUserType() )
                      .active( true )
                      .emailVerified( false )
                      .createdAt( LocalDateTime.now() )
                      .updatedAt( LocalDateTime.now() )
                      .profile( createDefaultProfile( command ) )
                      .build();

      log.debug( "User domain model created with ID: {}", user.getId().getValue() );

      User savedUser = saveUserPort.save( user );
      log.info( "User successfully saved to database: {}", savedUser.getId().getValue() );

      publishUserRegisteredEvent( savedUser );
      log.debug( "UserRegisteredEvent published" );

      UserResponse response = mapToUserResponse( savedUser );
      log.info( "User registration completed successfully for: {}", email.getMasked() );

      return response;
   }

   private void validateCommand( RegisterUserCommand command )
   {
      if ( command == null )
      {
         throw new IllegalArgumentException( "RegisterUserCommand cannot be null" );
      }
      if ( command.getEmail() == null || command.getEmail().isEmpty() )
      {
         throw new IllegalArgumentException( "Email is required" );
      }
      if ( command.getPassword() == null || command.getPassword().isEmpty() )
      {
         throw new IllegalArgumentException( "Password is required" );
      }
      if ( command.getUserType() == null )
      {
         throw new IllegalArgumentException( "UserType is required" );
      }
   }

   private UserProfile createDefaultProfile( RegisterUserCommand command )
   {
      return UserProfile.builder()
                        .firstName( command.getFirstName() )
                        .lastName( command.getLastName() )
                        .build();
   }

   private void publishUserRegisteredEvent( User user )
   {
      UserRegisteredEvent event = UserRegisteredEvent.builder()
                                                     .userId( user.getId().getValue() )
                                                     .email( user.getEmail().getValue() )
                                                     .userType( user.getUserType() )
                                                     .timestamp( LocalDateTime.now() )
                                                     .build();

      publishEventPort.publish( event, "user.registered" );
   }

   private UserResponse mapToUserResponse( User user )
   {
      return UserResponse.builder()
                         .id( user.getId().getValue() )
                         .email( user.getEmail().getValue() )
                         .userType( user.getUserType() )
                         .active( user.isActive() )
                         .emailVerified( user.isEmailVerified() )
                         .createdAt( user.getCreatedAt() )
                         .profile( user.getProfile() )
                         .build();
   }

   @lombok.Builder
   @lombok.Getter
   private static class UserRegisteredEvent
   {
      private String userId;
      private String email;
      private com.flexjob.user.domain.model.UserType userType;
      private LocalDateTime timestamp;
   }
}
