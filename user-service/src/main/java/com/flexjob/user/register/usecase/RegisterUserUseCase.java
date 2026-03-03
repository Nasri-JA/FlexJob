package com.flexjob.user.register.usecase;

import com.flexjob.user.register.dto.RegisterUserCommand;
import com.flexjob.user.shared.dto.UserResponse;

public interface RegisterUserUseCase
{
   UserResponse register( RegisterUserCommand command );

}
