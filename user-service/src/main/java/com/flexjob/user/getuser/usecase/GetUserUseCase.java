package com.flexjob.user.getuser.usecase;

import com.flexjob.user.shared.dto.UserResponse;
import com.flexjob.user.domain.vo.UserId;

public interface GetUserUseCase
{
   UserResponse getUserById( UserId userId );

   UserResponse getCurrentUser();
}
