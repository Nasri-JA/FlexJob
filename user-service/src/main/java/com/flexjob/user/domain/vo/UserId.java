package com.flexjob.user.domain.vo;

import java.util.Objects;
import java.util.UUID;

public final class UserId
{
   private final String value;

   private UserId( String value )
   {
      if ( value == null || value.isEmpty() )
      {
         throw new IllegalArgumentException( "UserId cannot be null or empty" );
      }
      this.value = value;
   }

   public static UserId of( String value )
   {
      return new UserId( value );
   }

   public static UserId of( UUID uuid )
   {
      return new UserId( uuid.toString() );
   }

   public static UserId generate()
   {
      return new UserId( UUID.randomUUID().toString() );
   }

   public String getValue()
   {
      return value;
   }

   public UUID asUuid()
   {
      return UUID.fromString( value );
   }

   @Override
   public boolean equals( Object o )
   {
      if ( this == o )
         return true;
      if ( o == null || getClass() != o.getClass() )
         return false;
      UserId userId = (UserId) o;
      return Objects.equals( value, userId.value );
   }

   @Override
   public int hashCode()
   {
      return Objects.hash( value );
   }

   @Override
   public String toString()
   {
      return "UserId{" + value + "}";
   }
}
