package com.flexjob.user.domain.exception;

public class UserNotFoundException extends RuntimeException
{
   private final String userId;

   public UserNotFoundException( String message )
   {
      super( message );
      this.userId = null;
   }

   public UserNotFoundException( String message, String userId )
   {
      super( message );
      this.userId = userId;
   }

   public UserNotFoundException( String message, Throwable cause )
   {
      super( message, cause );
      this.userId = null;
   }

   public UserNotFoundException( String message, String userId, Throwable cause )
   {
      super( message, cause );
      this.userId = userId;
   }

   public String getUserId()
   {
      return userId;
   }

   public static UserNotFoundException byId( String userId )
   {
      return new UserNotFoundException(
         "User mit ID nicht gefunden: " + userId,
         userId
      );
   }

   public static UserNotFoundException byEmail( String email )
   {
      return new UserNotFoundException(
         "User mit E-Mail nicht gefunden: " + email
      );
   }

   public static UserNotFoundException byUsername( String username )
   {
      return new UserNotFoundException(
         "User mit Username nicht gefunden: " + username
      );
   }
}
