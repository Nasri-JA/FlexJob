package com.flexjob.job.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobCategory
{
   private Long id;
   private String name;
   private String description;

   public static JobCategory create( String name, String description )
   {
      validateName( name );
      validateDescription( description );

      return JobCategory.builder()
                        .name( name.trim() )
                        .description( description != null ? description.trim() : null )
                        .build();
   }

   private static void validateName( String name )
   {
      if ( name == null || name.trim().isEmpty() )
      {
         throw new IllegalArgumentException( "Category name cannot be null or empty" );
      }

      String trimmed = name.trim();

      if ( trimmed.length() < 3 )
      {
         throw new IllegalArgumentException( "Category name must be at least 3 characters long" );
      }

      if ( trimmed.length() > 100 )
      {
         throw new IllegalArgumentException( "Category name cannot exceed 100 characters" );
      }
      if ( !trimmed.matches( "^[\\p{L}\\p{N}\\s-]+$" ) )
      {
         throw new IllegalArgumentException( "Category name contains invalid characters" );
      }
   }

   private static void validateDescription( String description )
   {
      if ( description != null && description.trim().length() > 500 )
      {
         throw new IllegalArgumentException( "Category description cannot exceed 500 characters" );
      }
   }

   public void update( String name, String description )
   {
      validateName( name );
      validateDescription( description );

      this.name = name.trim();
      this.description = description != null ? description.trim() : null;
   }

   public boolean nameMatches( String searchTerm )
   {
      if ( searchTerm == null || searchTerm.isEmpty() )
      {
         return false;
      }
      return name.toLowerCase().contains( searchTerm.toLowerCase() );
   }
}
