package com.flexjob.user.infrastructure.rest;

import com.flexjob.user.login.LoginCommand;
import com.flexjob.user.login.LoginResponse;
import com.flexjob.user.login.LoginUserUseCase;
import com.flexjob.user.register.RegisterUserCommand;
import com.flexjob.user.register.RegisterUserUseCase;
import com.flexjob.user.shared.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping( "/api/v1/auth" )
@RequiredArgsConstructor
@Slf4j
@CrossOrigin( origins = "*" )
public class AuthController
{
   private final RegisterUserUseCase registerUserUseCase;
   private final LoginUserUseCase loginUserUseCase;

   @PostMapping( "/register" )
   public ResponseEntity<UserResponse> register(
      @Valid @RequestBody RegisterUserCommand command
   )
   {
      log.info( "Registration request received for email: {}", command.getEmail() );

      UserResponse response = registerUserUseCase.register( command );

      return ResponseEntity
         .status( HttpStatus.CREATED )
         .body( response );
   }

   @PostMapping( "/login" )
   public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginCommand command
   )
   {
      log.info( "Login request received for email: {}", command.getEmail() );

      LoginResponse response = loginUserUseCase.login( command );

      return ResponseEntity.ok( response );
   }

}
