package com.flexjob.user.shared.repository;

import com.flexjob.user.domain.model.User;

public interface TokenGenerator
{
   String generateAccessToken( User user );

   String generateRefreshToken( User user );

   Long getAccessTokenExpirationSeconds();
}
