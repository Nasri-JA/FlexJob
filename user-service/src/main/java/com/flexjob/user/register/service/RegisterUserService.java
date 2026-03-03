package com.flexjob.user.register.service;

import com.flexjob.common.events.UserRegisteredEvent;
import com.flexjob.user.register.dto.RegisterUserCommand;
import com.flexjob.user.register.usecase.RegisterUserUseCase;
import com.flexjob.user.shared.repository.EventPublisher;
import com.flexjob.user.shared.repository.UserRepository;
import com.flexjob.user.shared.dto.UserResponse;
import com.flexjob.user.shared.mapper.UserResponseMapper;
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
   private final UserRepository userRepository;
   private final EventPublisher eventPublisher;
   private final UserDomainService domainService;
   private final PasswordEncoder passwordEncoder;
   private final UserResponseMapper userResponseMapper;

   @Override
   public UserResponse register( RegisterUserCommand command )
   {
      log.info( "Registering new user with email: {}", command.getEmail() );

      Email email = Email.of( command.getEmail() );
      log.debug( "Email validated and normalized: {}", email.getValue() );

      if ( userRepository.existsByEmail( email ) )
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

      User savedUser = userRepository.save( user );
      log.info( "User successfully saved to database: {}", savedUser.getId().getValue() );

      publishUserRegisteredEvent( savedUser );
      log.debug( "UserRegisteredEvent published" );

      UserResponse response = userResponseMapper.toUserResponse( savedUser );
      log.info( "User registration completed successfully for: {}", email.getMasked() );

      return response;
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
                                                     .userType( user.getUserType().name() )
                                                     .timestamp( LocalDateTime.now() )
                                                     .build();

      eventPublisher.publish( event, "user.registered" );
   }
}
