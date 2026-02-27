package com.flexjob.user.infrastructure.rest;

import com.flexjob.user.getuser.GetUserUseCase;
import com.flexjob.user.shared.UserResponse;
import com.flexjob.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/v1/users" )
@RequiredArgsConstructor
@Slf4j
@CrossOrigin( origins = "*" )
public class UserController
{
   private final GetUserUseCase getUserUseCase;

   @GetMapping( "/me" )
   @PreAuthorize( "isAuthenticated()" )
   public ResponseEntity<UserResponse> getCurrentUser()
   {
      log.info( "Get current user request" );

      UserResponse response = getUserUseCase.getCurrentUser();

      return ResponseEntity.ok( response );
   }

   @GetMapping( "/{id}" )
   @PreAuthorize( "isAuthenticated()" )
   public ResponseEntity<UserResponse> getUserById( @PathVariable String id )
   {
      log.info( "Get user by ID request: {}", id );

      UserId userId = UserId.of( id );
      UserResponse response = getUserUseCase.getUserById( userId );

      return ResponseEntity.ok( response );
   }

}
