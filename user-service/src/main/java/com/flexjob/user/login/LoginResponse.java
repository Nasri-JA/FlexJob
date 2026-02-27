package com.flexjob.user.login;

import com.flexjob.user.shared.UserResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse
{
   private String accessToken;
   private String refreshToken;
   private String tokenType = "Bearer";
   private Long expiresIn;
   private UserResponse user;
}
