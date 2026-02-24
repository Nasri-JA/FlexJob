package com.flexjob.user.application.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginCommand
{
   private final String email;
   private final String password;
}
