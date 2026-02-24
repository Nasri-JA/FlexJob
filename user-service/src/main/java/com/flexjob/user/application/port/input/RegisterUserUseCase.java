package com.flexjob.user.application.port.input;

import com.flexjob.user.application.dto.command.RegisterUserCommand;
import com.flexjob.user.application.dto.response.UserResponse;

public interface RegisterUserUseCase
{
   UserResponse register( RegisterUserCommand command );

}
