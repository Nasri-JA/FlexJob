package com.flexjob.job.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryCommand
{
   private Long categoryId;

   @NotBlank( message = "Category name is required" )
   @Size( min = 3, max = 100, message = "Category name must be between 3 and 100 characters" )
   private String name;

   @Size( max = 500, message = "Category description cannot exceed 500 characters" )
   private String description;
}
