package com.flexjob.job.domain.vo;

import java.util.Objects;

public final class Location
{
   private static final int MIN_LENGTH = 2;
   private static final int MAX_LENGTH = 255;

   private final String value;

   private Location( String value )
   {
      if ( value == null || value.isEmpty() )
      {
         throw new IllegalArgumentException( "Location cannot be null or empty" );
      }
      String trimmed = value.trim();
      if ( trimmed.length() < MIN_LENGTH )
      {
         throw new IllegalArgumentException(
            String.format( "Location must be at least %d characters long, got: '%s'",
                           MIN_LENGTH, trimmed )
         );
      }

      if ( trimmed.length() > MAX_LENGTH )
      {
         throw new IllegalArgumentException(
            String.format( "Location cannot exceed %d characters (got %d)",
                           MAX_LENGTH, trimmed.length() )
         );
      }

      this.value = trimmed;
   }

   public static Location of( String value )
   {
      return new Location( value );
   }

   public String getValue()
   {
      return value;
   }

   public String getUpperCase()
   {
      return value.toUpperCase();
   }

   public String getLowerCase()
   {
      return value.toLowerCase();
   }

   public boolean contains( String searchTerm )
   {
      if ( searchTerm == null || searchTerm.isEmpty() )
      {
         return false;
      }
      return value.toLowerCase().contains( searchTerm.toLowerCase() );
   }

   public boolean startsWith( String prefix )
   {
      if ( prefix == null || prefix.isEmpty() )
      {
         return false;
      }
      return value.toLowerCase().startsWith( prefix.toLowerCase() );
   }

   public String getCity()
   {
      int commaIndex = value.indexOf( ',' );
      if ( commaIndex > 0 )
      {
         return value.substring( 0, commaIndex ).trim();
      }
      return value;
   }

   public String getFormatted()
   {
      return value;
   }

   @Override
   public boolean equals( Object o )
   {
      if ( this == o )
         return true;
      if ( o == null || getClass() != o.getClass() )
         return false;
      Location location = (Location) o;
      return value.equalsIgnoreCase( location.value );
   }

   @Override
   public int hashCode()
   {
      return Objects.hash( value.toLowerCase() );
   }

   @Override
   public String toString()
   {
      return "Location{" + value + "}";
   }
}
