package com.flexjob.job.application.port.input;

import com.flexjob.job.application.dto.command.CreateCategoryCommand;
import com.flexjob.job.application.dto.command.UpdateCategoryCommand;
import com.flexjob.job.application.dto.response.CategoryResponse;

import java.util.List;

public interface ManageCategoriesUseCase
{
   CategoryResponse createCategory( CreateCategoryCommand command );

   CategoryResponse updateCategory( UpdateCategoryCommand command );

   void deleteCategory( Long categoryId );

   CategoryResponse getCategoryById( Long categoryId );

   List<CategoryResponse> getAllCategories();
}
