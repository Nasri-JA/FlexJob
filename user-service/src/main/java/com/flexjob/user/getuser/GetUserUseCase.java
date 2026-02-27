package com.flexjob.user.getuser;

import com.flexjob.user.shared.UserResponse;
import com.flexjob.user.domain.vo.UserId;

public interface GetUserUseCase
{
   UserResponse getUserById( UserId userId );

   UserResponse getCurrentUser();
}
