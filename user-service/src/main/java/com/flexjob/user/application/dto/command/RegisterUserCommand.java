package com.flexjob.user.application.dto.command;

import com.flexjob.user.domain.model.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterUserCommand
{
   private final String email;
   private final String password;
   private final UserType userType;
   private final String firstName;
   private final String lastName;

}
