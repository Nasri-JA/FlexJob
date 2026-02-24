package com.flexjob.user.domain.service;

import com.flexjob.user.domain.exception.UserNotFoundException;
import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService
{
   public void validateRegistration( Email email, String password )
   {
      validatePasswordStrength( password );
      validateEmailDomain( email );
   }

   private void validatePasswordStrength( String password )
   {
      if ( password == null || password.length() < 8 )
      {
         throw new IllegalArgumentException(
            "Passwort muss mindestens 8 Zeichen lang sein"
         );
      }

      if ( !password.matches( ".*[A-Z].*" ) )
      {
         throw new IllegalArgumentException(
            "Passwort muss mindestens einen Großbuchstaben enthalten"
         );
      }

      if ( !password.matches( ".*[a-z].*" ) )
      {
         throw new IllegalArgumentException(
            "Passwort muss mindestens einen Kleinbuchstaben enthalten"
         );
      }

      if ( !password.matches( ".*\\d.*" ) )
      {
         throw new IllegalArgumentException(
            "Passwort muss mindestens eine Zahl enthalten"
         );
      }

      if ( !password.matches( ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*" ) )
      {
         throw new IllegalArgumentException(
            "Passwort muss mindestens ein Sonderzeichen enthalten"
         );
      }
   }

   private void validateEmailDomain( Email email )
   {
      String domain = email.getDomain();

      String[] blockedDomains = {
         "tempmail.com",
         "throwaway.email",
         "guerrillamail.com",
         "10minutemail.com"
      };

      for ( String blockedDomain : blockedDomains )
      {
         if ( domain.equalsIgnoreCase( blockedDomain ) )
         {
            throw new IllegalArgumentException(
               "E-Mail-Domain ist nicht erlaubt: " + domain
            );
         }
      }

   }

   public void validateUserActive( User user )
   {
      if ( !user.isActive() )
      {
         throw new IllegalStateException(
            "User-Account ist deaktiviert: " + user.getId().getValue()
         );
      }
   }

   public User requireUserExists( User user, UserId userId )
   {
      if ( user == null )
      {
         throw new UserNotFoundException(
            "User nicht gefunden: " + userId.getValue()
         );
      }
      return user;
   }

   public void requireEmailVerified( User user )
   {
      if ( !user.isEmailVerified() )
      {
         throw new IllegalStateException(
            "E-Mail muss verifiziert sein. Bitte prüfen Sie Ihren Posteingang."
         );
      }
   }

   public boolean isSameUser( User user1, User user2 )
   {
      if ( user1 == null || user2 == null )
      {
         return false;
      }
      return user1.getId().equals( user2.getId() );
   }
}
