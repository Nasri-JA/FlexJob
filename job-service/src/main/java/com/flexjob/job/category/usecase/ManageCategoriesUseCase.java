package com.flexjob.job.category.usecase;

import com.flexjob.job.category.dto.CreateCategoryCommand;
import com.flexjob.job.category.dto.UpdateCategoryCommand;
import com.flexjob.job.shared.dto.CategoryResponse;

import java.util.List;

public interface ManageCategoriesUseCase
{
   CategoryResponse createCategory( CreateCategoryCommand command );

   CategoryResponse updateCategory( UpdateCategoryCommand command );

   void deleteCategory( Long categoryId );

   CategoryResponse getCategoryById( Long categoryId );

   List<CategoryResponse> getAllCategories();
}
