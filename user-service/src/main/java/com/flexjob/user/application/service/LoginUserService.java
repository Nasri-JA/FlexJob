package com.flexjob.user.application.service;

import com.flexjob.user.application.dto.command.LoginCommand;
import com.flexjob.user.application.dto.response.LoginResponse;
import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.application.port.input.LoginUserUseCase;
import com.flexjob.user.application.port.output.LoadUserPort;
import com.flexjob.user.domain.exception.UserNotFoundException;
import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.service.UserDomainService;
import com.flexjob.user.domain.vo.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional( readOnly = true )
public class LoginUserService implements LoginUserUseCase
{
   private final LoadUserPort loadUserPort;
   private final UserDomainService domainService;
   private final PasswordEncoder passwordEncoder;
   private final JwtTokenGenerator jwtTokenGenerator;

   @Override
   public LoginResponse login( LoginCommand command )
   {
      log.info( "Login attempt for email: {}", command.getEmail() );

      Email email = Email.of( command.getEmail() );

      User user = loadUserPort.loadByEmail( email )
                              .orElseThrow( () -> {
                                 log.warn( "Login failed: User not found for email: {}", email.getMasked() );
                                 throw new UserNotFoundException( "Ungültige E-Mail oder Passwort" );
                              } );

      if ( !passwordEncoder.matches( command.getPassword(), user.getPasswordHash() ) )
      {
         log.warn( "Login failed: Invalid password for user: {}", user.getId().getValue() );
         throw new IllegalArgumentException( "Ungültige E-Mail oder Passwort" );
      }

      domainService.validateUserActive( user );

      log.info( "Login successful for user: {}", user.getId().getValue() );

      String accessToken = jwtTokenGenerator.generateAccessToken( user );
      String refreshToken = jwtTokenGenerator.generateRefreshToken( user );

      return LoginResponse.builder()
                          .accessToken( accessToken )
                          .refreshToken( refreshToken )
                          .tokenType( "Bearer" )
                          .expiresIn( jwtTokenGenerator.getAccessTokenExpirationSeconds() )
                          .user( mapToUserResponse( user ) )
                          .build();
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
                         .updatedAt( user.getUpdatedAt() )
                         .profile( user.getProfile() )
                         .build();
   }
}
