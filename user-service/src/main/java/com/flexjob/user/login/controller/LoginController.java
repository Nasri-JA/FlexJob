package com.flexjob.user.login.controller;

import com.flexjob.user.login.dto.LoginCommand;
import com.flexjob.user.login.dto.LoginResponse;
import com.flexjob.user.login.usecase.LoginUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping( "/api/v1/auth" )
@RequiredArgsConstructor
@Slf4j
@CrossOrigin( origins = "*" )
public class LoginController
{
   private final LoginUserUseCase loginUserUseCase;

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
