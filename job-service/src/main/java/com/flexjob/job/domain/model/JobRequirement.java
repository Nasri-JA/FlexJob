package com.flexjob.job.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobRequirement
{
   private Long id;
   private String requirementText;

   @Builder.Default
   private boolean isMandatory = true;

   public static JobRequirement create( String requirementText, boolean isMandatory )
   {
      validateRequirementText( requirementText );

      return JobRequirement.builder()
                           .requirementText( requirementText.trim() )
                           .isMandatory( isMandatory )
                           .build();
   }

   public static JobRequirement mandatory( String requirementText )
   {
      return create( requirementText, true );
   }

   public static JobRequirement optional( String requirementText )
   {
      return create( requirementText, false );
   }

   private static void validateRequirementText( String requirementText )
   {
      if ( requirementText == null || requirementText.trim().isEmpty() )
      {
         throw new IllegalArgumentException( "Requirement text cannot be null or empty" );
      }

      String trimmed = requirementText.trim();

      if ( trimmed.length() < 5 )
      {
         throw new IllegalArgumentException(
            "Requirement text must be at least 5 characters long (got: " + trimmed.length() + ")"
         );
      }

      if ( trimmed.length() > 500 )
      {
         throw new IllegalArgumentException(
            "Requirement text cannot exceed 500 characters (got: " + trimmed.length() + ")"
         );
      }
   }

   public void toggleMandatory()
   {
      this.isMandatory = !this.isMandatory;
   }

   public void setAsMandatory()
   {
      this.isMandatory = true;
   }

   public void setAsOptional()
   {
      this.isMandatory = false;
   }

   public void updateText( String newText )
   {
      validateRequirementText( newText );
      this.requirementText = newText.trim();
   }

   public boolean textContains( String searchTerm )
   {
      if ( searchTerm == null || searchTerm.isEmpty() )
      {
         return false;
      }
      return requirementText.toLowerCase().contains( searchTerm.toLowerCase() );
   }

   public String getFormattedText()
   {
      return ( isMandatory ? "* " : "  " ) + requirementText;
   }
}
