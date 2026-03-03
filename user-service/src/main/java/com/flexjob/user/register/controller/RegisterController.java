package com.flexjob.user.register.controller;

import com.flexjob.user.register.dto.RegisterUserCommand;
import com.flexjob.user.register.usecase.RegisterUserUseCase;
import com.flexjob.user.shared.dto.UserResponse;
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
public class RegisterController
{
   private final RegisterUserUseCase registerUserUseCase;

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

}
