package com.flexjob.user.application.port.input;

import com.flexjob.user.application.dto.command.LoginCommand;
import com.flexjob.user.application.dto.response.LoginResponse;

public interface LoginUserUseCase
{
   LoginResponse login( LoginCommand command );

}
