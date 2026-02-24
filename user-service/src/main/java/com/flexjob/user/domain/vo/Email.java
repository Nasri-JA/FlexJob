package com.flexjob.user.domain.vo;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Email
{
   private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
   );

   private final String value;

   private Email( String value )
   {
      if ( value == null || value.isEmpty() )
      {
         throw new IllegalArgumentException( "Email cannot be null or empty" );
      }

      String normalized = value.trim().toLowerCase();

      if ( !EMAIL_PATTERN.matcher( normalized ).matches() )
      {
         throw new IllegalArgumentException( "Invalid email format: " + value );
      }

      if ( normalized.length() > 255 )
      {
         throw new IllegalArgumentException( "Email too long (max 255 characters)" );
      }

      this.value = normalized;
   }

   public static Email of( String value )
   {
      return new Email( value );
   }

   public String getValue()
   {
      return value;
   }

   public String getDomain()
   {
      int atIndex = value.indexOf( '@' );
      return value.substring( atIndex + 1 );
   }

   public String getLocalPart()
   {
      int atIndex = value.indexOf( '@' );
      return value.substring( 0, atIndex );
   }

   public boolean isFromDomain( String domain )
   {
      return getDomain().equalsIgnoreCase( domain );
   }

   public String getMasked()
   {
      String localPart = getLocalPart();
      if ( localPart.length() <= 1 )
      {
         return localPart + "***@" + getDomain();
      }
      return localPart.charAt( 0 ) + "***@" + getDomain();
   }

   @Override
   public boolean equals( Object o )
   {
      if ( this == o )
         return true;
      if ( o == null || getClass() != o.getClass() )
         return false;
      Email email = (Email) o;
      return Objects.equals( value, email.value );
   }

   @Override
   public int hashCode()
   {
      return Objects.hash( value );
   }

   @Override
   public String toString()
   {
      return "Email{" + getMasked() + "}";
   }
}
