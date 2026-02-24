package com.flexjob.user.infrastructure.adapter.input.rest.mapper;

import com.flexjob.user.application.dto.command.LoginCommand;
import com.flexjob.user.application.dto.command.RegisterUserCommand;
import com.flexjob.user.infrastructure.adapter.input.rest.dto.LoginRequest;
import com.flexjob.user.infrastructure.adapter.input.rest.dto.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRestMapper
{
   public RegisterUserCommand toRegisterCommand( RegisterRequest request )
   {
      return RegisterUserCommand.builder()
                                .email( request.getEmail() )
                                .password( request.getPassword() )
                                .userType( request.getUserType() )
                                .firstName( request.getFirstName() )
                                .lastName( request.getLastName() )
                                .build();
   }

   public LoginCommand toLoginCommand( LoginRequest request )
   {
      return LoginCommand.builder()
                         .email( request.getEmail() )
                         .password( request.getPassword() )
                         .build();
   }

}
