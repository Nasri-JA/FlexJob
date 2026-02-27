package com.flexjob.user.infrastructure.config;

import com.flexjob.user.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
   private final JwtTokenProvider jwtTokenProvider;

   @Override
   protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
   ) throws ServletException, IOException
   {

      try
      {
         String token = extractTokenFromRequest( request );

         if ( token != null && jwtTokenProvider.validateToken( token ) )
         {

            Authentication authentication = jwtTokenProvider.getAuthentication( token );

            SecurityContextHolder.getContext().setAuthentication( authentication );

            log.debug( "JWT authentication successful for user: {}",
                       authentication.getName() );
         }

      }
      catch ( Exception e )
      {
         log.error( "JWT authentication failed: {}", e.getMessage() );
      }

      filterChain.doFilter( request, response );
   }

   private String extractTokenFromRequest( HttpServletRequest request )
   {
      String bearerToken = request.getHeader( "Authorization" );

      if ( StringUtils.hasText( bearerToken ) && bearerToken.startsWith( "Bearer " ) )
      {
         return bearerToken.substring( 7 );
      }

      return null;
   }

   @Override
   protected boolean shouldNotFilterErrorDispatch()
   {
      return false;
   }

}
