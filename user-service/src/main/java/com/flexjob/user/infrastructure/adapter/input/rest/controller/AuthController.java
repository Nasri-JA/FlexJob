package com.flexjob.user.infrastructure.adapter.input.rest.controller;

import com.flexjob.user.application.dto.command.LoginCommand;
import com.flexjob.user.application.dto.command.RegisterUserCommand;
import com.flexjob.user.application.dto.response.LoginResponse;
import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.application.port.input.LoginUserUseCase;
import com.flexjob.user.application.port.input.RegisterUserUseCase;
import com.flexjob.user.infrastructure.adapter.input.rest.dto.LoginRequest;
import com.flexjob.user.infrastructure.adapter.input.rest.dto.RegisterRequest;
import com.flexjob.user.infrastructure.adapter.input.rest.mapper.UserRestMapper;
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
   private final UserRestMapper mapper;

   @PostMapping( "/register" )
   public ResponseEntity<UserResponse> register(
      @Valid @RequestBody RegisterRequest request
   )
   {
      log.info( "Registration request received for email: {}", request.getEmail() );

      RegisterUserCommand command = mapper.toRegisterCommand( request );
      UserResponse response = registerUserUseCase.register( command );

      return ResponseEntity
         .status( HttpStatus.CREATED )
         .body( response );
   }

   @PostMapping( "/login" )
   public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest request
   )
   {
      log.info( "Login request received for email: {}", request.getEmail() );

      LoginCommand command = mapper.toLoginCommand( request );

      LoginResponse response = loginUserUseCase.login( command );

      return ResponseEntity.ok( response );
   }

}
