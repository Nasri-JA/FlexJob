package com.flexjob.job.domain.vo;

import java.util.Objects;

public final class JobDescription
{
   private static final int MIN_LENGTH = 20;
   private static final int MAX_LENGTH = 5000;
   private static final int SUMMARY_LENGTH = 200;

   private final String value;

   private JobDescription( String value )
   {
      if ( value == null || value.isEmpty() )
      {
         throw new IllegalArgumentException( "Job description cannot be null or empty" );
      }
      String normalized = normalizeDescription( value );
      if ( normalized.length() < MIN_LENGTH )
      {
         throw new IllegalArgumentException(
            String.format( "Job description must be at least %d characters long (got %d)",
                           MIN_LENGTH, normalized.length() )
         );
      }

      if ( normalized.length() > MAX_LENGTH )
      {
         throw new IllegalArgumentException(
            String.format( "Job description cannot exceed %d characters (got %d)",
                           MAX_LENGTH, normalized.length() )
         );
      }

      this.value = normalized;
   }

   private String normalizeDescription( String description )
   {
      String cleaned = description.replace( '\t', ' ' );
      cleaned = cleaned.replaceAll( "(\r?\n){3,}", "\n\n" );
      cleaned = cleaned.replaceAll( " +", " " );
      return cleaned.trim();
   }

   public static JobDescription of( String value )
   {
      return new JobDescription( value );
   }

   public String getValue()
   {
      return value;
   }

   public int getLength()
   {
      return value.length();
   }

   public String getSummary()
   {
      return truncate( SUMMARY_LENGTH );
   }

   public String truncate( int maxLength )
   {
      if ( value.length() <= maxLength )
      {
         return value;
      }

      // Suche letztes Leerzeichen vor maxLength (vermeidet Wort-Trennung)
      int lastSpace = value.lastIndexOf( ' ', maxLength - 3 );
      if ( lastSpace > 0 && lastSpace > maxLength - 50 )
      {
         // Nur wenn Leerzeichen nahe genug (nicht zu weit zurück)
         return value.substring( 0, lastSpace ) + "...";
      }

      // Fallback: Harte Trennung
      return value.substring( 0, maxLength - 3 ) + "...";
   }

   public boolean contains( String searchTerm )
   {
      if ( searchTerm == null || searchTerm.isEmpty() )
      {
         return false;
      }
      return value.toLowerCase().contains( searchTerm.toLowerCase() );
   }

   public String getPlainText()
   {
      String plain = value
         .replaceAll( "\\*\\*(.+?)\\*\\*", "$1" ) // Bold: **text** -> text
         .replaceAll( "\\*(.+?)\\*", "$1" )       // Italic: *text* -> text
         .replaceAll( "\\[(.+?)\\]\\(.+?\\)", "$1" ) // Links: [text](url) -> text
         .replaceAll( "#+\\s*", "" );              // Headers: ## Header -> Header

      return plain;
   }

   public int getWordCount()
   {
      String plainText = getPlainText().trim();
      if ( plainText.isEmpty() )
      {
         return 0;
      }
      return plainText.split( "\\s+" ).length;
   }

   public int getEstimatedReadingTimeMinutes()
   {
      int words = getWordCount();
      int minutes = (int) Math.ceil( words / 200.0 );
      return Math.max( 1, minutes ); // Mindestens 1 Minute
   }

   public boolean isDetailed()
   {
      return getWordCount() >= 100;
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
      JobDescription that = (JobDescription) o;
      return Objects.equals( value, that.value );
   }

   @Override
   public int hashCode()
   {
      return Objects.hash( value );
   }

   @Override
   public String toString()
   {
      return "JobDescription{" + truncate( 50 ) + "}";
   }
}
