package com.flexjob.job.category.service;

import com.flexjob.job.category.dto.CreateCategoryCommand;
import com.flexjob.job.category.dto.UpdateCategoryCommand;
import com.flexjob.job.category.usecase.ManageCategoriesUseCase;
import com.flexjob.job.domain.exception.CategoryNotFoundException;
import com.flexjob.job.domain.model.JobCategory;
import com.flexjob.job.shared.dto.CategoryResponse;
import com.flexjob.job.shared.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements ManageCategoriesUseCase
{

   private final CategoryRepository categoryRepository;

   @Override
   @Transactional
   public CategoryResponse createCategory( CreateCategoryCommand command )
   {
      log.info( "Creating category with name: {}", command.getName() );

      if ( categoryRepository.existsByName( command.getName() ) )
      {
         throw new IllegalArgumentException( "Category already exists with name: " + command.getName() );
      }

      JobCategory category = JobCategory.create( command.getName(), command.getDescription() );
      JobCategory savedCategory = categoryRepository.save( category );

      log.info( "Category created successfully with ID: {}", savedCategory.getId() );

      return mapToResponse( savedCategory );
   }

   @Override
   @Transactional
   public CategoryResponse updateCategory( UpdateCategoryCommand command )
   {
      log.info( "Updating category ID {} with name: {}", command.getCategoryId(), command.getName() );

      JobCategory category = categoryRepository.findById( command.getCategoryId() )
                                               .orElseThrow( () -> CategoryNotFoundException.byId( command.getCategoryId() ) );

      if ( !category.getName().equals( command.getName() ) &&
         categoryRepository.existsByName( command.getName() ) )
      {
         throw new IllegalArgumentException( "Category already exists with name: " + command.getName() );
      }

      category.update( command.getName(), command.getDescription() );
      JobCategory updatedCategory = categoryRepository.save( category );

      log.info( "Category updated successfully with ID: {}", updatedCategory.getId() );

      return mapToResponse( updatedCategory );
   }

   @Override
   @Transactional
   public void deleteCategory( Long categoryId )
   {
      log.info( "Deleting category ID: {}", categoryId );

      categoryRepository.findById( categoryId )
                        .orElseThrow( () -> CategoryNotFoundException.byId( categoryId ) );

      long jobCount = categoryRepository.countJobsInCategory( categoryId );
      if ( jobCount > 0 )
      {
         throw new IllegalStateException(
            String.format( "Cannot delete category: %d job(s) still using it", jobCount )
         );
      }

      categoryRepository.delete( categoryId );

      log.info( "Category deleted successfully with ID: {}", categoryId );
   }

   @Override
   @Transactional( readOnly = true )
   public CategoryResponse getCategoryById( Long categoryId )
   {
      log.debug( "Fetching category with ID: {}", categoryId );

      JobCategory category = categoryRepository.findById( categoryId )
                                               .orElseThrow( () -> CategoryNotFoundException.byId( categoryId ) );

      return mapToResponse( category );
   }

   @Override
   @Transactional( readOnly = true )
   public List<CategoryResponse> getAllCategories()
   {
      log.debug( "Fetching all categories" );

      return categoryRepository.findAll().stream()
                               .map( this :: mapToResponse )
                               .toList();
   }

   private CategoryResponse mapToResponse( JobCategory category )
   {
      long jobCount = categoryRepository.countJobsInCategory( category.getId() );

      return CategoryResponse.builder()
                             .id( category.getId() )
                             .name( category.getName() )
                             .description( category.getDescription() )
                             .jobCount( jobCount )
                             .build();
   }
}
