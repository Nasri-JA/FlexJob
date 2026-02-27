package com.flexjob.user.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider
{
   @Value( "${jwt.secret:your-256-bit-secret-key-change-this-in-production}" )
   private String secret;

   public boolean validateToken( String token )
   {
      try
      {
         Jwts.parser()
             .verifyWith( getSigningKey() )
             .build()
             .parseSignedClaims( token );

         return true;

      }
      catch ( Exception e )
      {
         log.error( "Invalid JWT token: {}", e.getMessage() );
         return false;
      }
   }

   public String getUserIdFromToken( String token )
   {
      Claims claims = getClaims( token );
      return claims.getSubject();
   }

   public Authentication getAuthentication( String token )
   {
      Claims claims = getClaims( token );

      String userId = claims.getSubject();

      String roles = (String) claims.get( "roles" );
      List<SimpleGrantedAuthority> authorities = Collections.singletonList(
         new SimpleGrantedAuthority( roles )
      );

      return new UsernamePasswordAuthenticationToken( userId, token, authorities );
   }

   private Claims getClaims( String token )
   {
      return Jwts.parser()
                 .verifyWith( getSigningKey() )
                 .build()
                 .parseSignedClaims( token )
                 .getPayload();
   }

   private SecretKey getSigningKey()
   {
      return Keys.hmacShaKeyFor( secret.getBytes() );
   }
}
