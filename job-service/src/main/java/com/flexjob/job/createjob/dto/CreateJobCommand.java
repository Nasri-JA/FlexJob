package com.flexjob.job.createjob.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobCommand
{
   private Long employerId;

   @NotNull( message = "Category ID is required" )
   @Positive( message = "Category ID must be positive" )
   private Long categoryId;

   @NotBlank( message = "Title is required" )
   @Size( min = 5, max = 200, message = "Title must be between 5 and 200 characters" )
   private String title;

   @NotBlank( message = "Description is required" )
   @Size( min = 20, max = 5000, message = "Description must be between 20 and 5000 characters" )
   private String description;

   @NotBlank( message = "Location is required" )
   @Size( min = 2, max = 255, message = "Location must be between 2 and 255 characters" )
   private String location;

   @NotNull( message = "Hourly rate is required" )
   @DecimalMin( value = "10.0", message = "Hourly rate must be at least 10.00 EUR" )
   @DecimalMax( value = "1000.0", message = "Hourly rate cannot exceed 1000.00 EUR" )
   private Double hourlyRate;

   @NotNull( message = "Duration hours is required" )
   @Min( value = 1, message = "Duration must be at least 1 hour" )
   @Max( value = 2000, message = "Duration cannot exceed 2000 hours" )
   private Integer durationHours;

   @Valid
   private List<RequirementCommand> requirements;

   @Getter
   @Setter
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class RequirementCommand
   {
      @NotBlank( message = "Requirement text is required" )
      @Size( min = 5, max = 500, message = "Requirement text must be between 5 and 500 characters" )
      private String requirementText;

      @Builder.Default
      private Boolean isMandatory = true;
   }
}
