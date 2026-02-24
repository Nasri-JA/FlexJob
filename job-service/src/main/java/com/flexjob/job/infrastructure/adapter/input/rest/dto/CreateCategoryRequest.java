package com.flexjob.job.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequest
{
   @NotBlank( message = "Category name is required" )
   @Size( min = 3, max = 100, message = "Category name must be between 3 and 100 characters" )
   private String name;

   @Size( max = 500, message = "Category description cannot exceed 500 characters" )
   private String description;
}
