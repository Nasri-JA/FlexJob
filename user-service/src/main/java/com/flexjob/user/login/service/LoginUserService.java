package com.flexjob.user.login.service;

import com.flexjob.user.login.dto.LoginCommand;
import com.flexjob.user.login.dto.LoginResponse;
import com.flexjob.user.login.usecase.LoginUserUseCase;
import com.flexjob.user.shared.repository.TokenGenerator;
import com.flexjob.user.shared.repository.UserRepository;
import com.flexjob.user.shared.mapper.UserResponseMapper;
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
   private final UserRepository userRepository;
   private final UserDomainService domainService;
   private final PasswordEncoder passwordEncoder;
   private final TokenGenerator tokenGenerator;
   private final UserResponseMapper userResponseMapper;

   @Override
   public LoginResponse login( LoginCommand command )
   {
      log.info( "Login attempt for email: {}", command.getEmail() );

      Email email = Email.of( command.getEmail() );

      User user = userRepository.loadByEmail( email )
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

      String accessToken = tokenGenerator.generateAccessToken( user );
      String refreshToken = tokenGenerator.generateRefreshToken( user );

      return LoginResponse.builder()
                          .accessToken( accessToken )
                          .refreshToken( refreshToken )
                          .tokenType( "Bearer" )
                          .expiresIn( tokenGenerator.getAccessTokenExpirationSeconds() )
                          .user( userResponseMapper.toUserResponse( user ) )
                          .build();
   }
}
