package com.flexjob.user.application.service;

import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.application.port.input.GetUserUseCase;
import com.flexjob.user.application.port.output.LoadUserPort;
import com.flexjob.user.domain.exception.UserNotFoundException;
import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.service.UserDomainService;
import com.flexjob.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional( readOnly = true )
public class GetUserService implements GetUserUseCase
{
   private final LoadUserPort loadUserPort;
   private final UserDomainService domainService;

   @Override
   public UserResponse getUserById( UserId userId )
   {
      log.info( "Getting user by ID: {}", userId.getValue() );

      User user = loadUserPort.loadById( userId )
                              .orElseThrow( () -> UserNotFoundException.byId( userId.getValue() ) );

      return mapToUserResponse( user );
   }

   @Override
   public UserResponse getCurrentUser()
   {
      log.info( "Getting current authenticated user" );

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String userIdString = authentication.getName();

      UserId userId = UserId.of( userIdString );

      return getUserById( userId );
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
