package com.flexjob.user.application.port.input;

import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.domain.vo.UserId;

public interface GetUserUseCase
{
   UserResponse getUserById( UserId userId );

   UserResponse getCurrentUser();
}
