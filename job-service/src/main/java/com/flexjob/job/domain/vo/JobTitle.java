package com.flexjob.job.domain.vo;

import java.util.Objects;

public final class JobTitle
{
   private static final int MIN_LENGTH = 5;
   private static final int MAX_LENGTH = 200;

   private final String value;

   private JobTitle( String value )
   {
      if ( value == null || value.isEmpty() )
      {
         throw new IllegalArgumentException( "Job title cannot be null or empty" );
      }
      String normalized = normalizeTitle( value );
      if ( normalized.length() < MIN_LENGTH )
      {
         throw new IllegalArgumentException(
            String.format( "Job title must be at least %d characters long (got %d): '%s'",
                           MIN_LENGTH, normalized.length(), normalized )
         );
      }

      if ( normalized.length() > MAX_LENGTH )
      {
         throw new IllegalArgumentException(
            String.format( "Job title cannot exceed %d characters (got %d)",
                           MAX_LENGTH, normalized.length() )
         );
      }
      if ( !isValidFormat( normalized ) )
      {
         throw new IllegalArgumentException( "Job title contains invalid characters: " + normalized );
      }

      this.value = normalized;
   }

   private String normalizeTitle( String title )
   {
      String cleaned = title.replaceAll( "[\t\n\r]", " " );
      cleaned = cleaned.replaceAll( "\\s+", " " );
      return cleaned.trim();
   }

   private boolean isValidFormat( String title )
   {
      return title.matches( "^[\\p{L}\\p{N}\\s\\-()&/.,]+$" );
   }

   public static JobTitle of( String value )
   {
      return new JobTitle( value );
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

   public String getCapitalized()
   {
      if ( value.isEmpty() )
      {
         return value;
      }
      return value.substring( 0, 1 ).toUpperCase() + value.substring( 1 );
   }

   public boolean contains( String searchTerm )
   {
      if ( searchTerm == null || searchTerm.isEmpty() )
      {
         return false;
      }
      return value.toLowerCase().contains( searchTerm.toLowerCase() );
   }

   public String[] getKeywords()
   {
      return value.toLowerCase()
                  .split( "\\s+" );
   }

   public String toSlug()
   {
      return value.toLowerCase()
                  .replaceAll( "[^\\p{L}\\p{N}\\s-]", "" )
                  .replaceAll( "\\s+", "-" ) // Ersetze Leerzeichen durch Bindestrich
                  .replaceAll( "-+", "-" )
                  .replaceAll( "^-|-$", "" );
   }

   public String truncate( int maxLength )
   {
      if ( value.length() <= maxLength )
      {
         return value;
      }
      return value.substring( 0, maxLength - 3 ) + "...";
   }

   @Override
   public boolean equals( Object o )
   {
      if ( this == o )
         return true;
      if ( o == null || getClass() != o.getClass() )
         return false;
      JobTitle jobTitle = (JobTitle) o;
      return Objects.equals( value, jobTitle.value );
   }

   @Override
   public int hashCode()
   {
      return Objects.hash( value );
   }

   @Override
   public String toString()
   {
      return "JobTitle{" + value + "}";
   }
}
