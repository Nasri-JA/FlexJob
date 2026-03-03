package com.flexjob.user.login.usecase;

import com.flexjob.user.login.dto.LoginCommand;
import com.flexjob.user.login.dto.LoginResponse;

public interface LoginUserUseCase
{
   LoginResponse login( LoginCommand command );

}
