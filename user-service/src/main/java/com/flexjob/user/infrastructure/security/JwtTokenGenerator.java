package com.flexjob.user.infrastructure.security;

import com.flexjob.user.shared.TokenGenerator;
import com.flexjob.user.domain.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenGenerator implements TokenGenerator
{
   @Value( "${jwt.secret:your-256-bit-secret-key-change-this-in-production}" )
   private String secret;

   @Value( "${jwt.access-token-expiration:900000}" )
   private Long accessTokenExpiration;

   @Value( "${jwt.refresh-token-expiration:604800000}" )
   private Long refreshTokenExpiration;

   @Override
   public String generateAccessToken( User user )
   {
      Map<String, Object> claims = new HashMap<>();
      claims.put( "email", user.getEmail().getValue() );
      claims.put( "userType", user.getUserType().name() );
      claims.put( "roles", user.getUserType().getRole() );

      return Jwts.builder()
                 .setClaims( claims )
                 .setSubject( user.getId().getValue() ).setIssuedAt( new Date() )
                 .setExpiration( new Date( System.currentTimeMillis() + accessTokenExpiration ) )
                 .signWith( getSigningKey(), SignatureAlgorithm.HS256 ).compact();
   }

   @Override
   public String generateRefreshToken( User user )
   {
      return Jwts.builder()
                 .setSubject( user.getId().getValue() )
                 .setIssuedAt( new Date() )
                 .setExpiration( new Date( System.currentTimeMillis() + refreshTokenExpiration ) )
                 .signWith( getSigningKey(), SignatureAlgorithm.HS256 )
                 .compact();
   }

   @Override
   public Long getAccessTokenExpirationSeconds()
   {
      return accessTokenExpiration / 1000;
   }

   private Key getSigningKey()
   {
      return Keys.hmacShaKeyFor( secret.getBytes() );
   }

}
