package com.flexjob.user.register;

import com.flexjob.user.shared.UserResponse;

public interface RegisterUserUseCase
{
   UserResponse register( RegisterUserCommand command );

}
