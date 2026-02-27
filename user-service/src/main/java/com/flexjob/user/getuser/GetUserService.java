package com.flexjob.user.getuser;

import com.flexjob.user.shared.UserRepository;
import com.flexjob.user.shared.UserResponse;
import com.flexjob.user.shared.UserResponseMapper;
import com.flexjob.user.domain.exception.UserNotFoundException;
import com.flexjob.user.domain.model.User;
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
   private final UserRepository userRepository;
   private final UserResponseMapper userResponseMapper;

   @Override
   public UserResponse getUserById( UserId userId )
   {
      log.info( "Getting user by ID: {}", userId.getValue() );

      User user = userRepository.loadById( userId )
                              .orElseThrow( () -> UserNotFoundException.byId( userId.getValue() ) );

      return userResponseMapper.toUserResponse( user );
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
}
