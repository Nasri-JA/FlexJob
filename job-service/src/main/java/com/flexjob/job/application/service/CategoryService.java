package com.flexjob.job.application.service;

import com.flexjob.job.application.dto.command.CreateCategoryCommand;
import com.flexjob.job.application.dto.command.UpdateCategoryCommand;
import com.flexjob.job.application.dto.response.CategoryResponse;
import com.flexjob.job.application.port.input.ManageCategoriesUseCase;
import com.flexjob.job.application.port.output.LoadCategoryPort;
import com.flexjob.job.application.port.output.SaveCategoryPort;
import com.flexjob.job.domain.exception.CategoryNotFoundException;
import com.flexjob.job.domain.model.JobCategory;
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

   private final LoadCategoryPort loadCategoryPort;
   private final SaveCategoryPort saveCategoryPort;

   @Override
   @Transactional
   public CategoryResponse createCategory( CreateCategoryCommand command )
   {
      log.info( "Creating category with name: {}", command.getName() );
      if ( loadCategoryPort.existsByName( command.getName() ) )
      {
         log.error( "Category already exists with name: {}", command.getName() );
         throw new IllegalArgumentException( "Category already exists with name: " + command.getName() );
      }
      JobCategory category = JobCategory.create( command.getName(), command.getDescription() );

      log.debug( "Category entity created: {}", category.getName() );
      JobCategory savedCategory = saveCategoryPort.save( category );

      log.info( "Category created successfully with ID: {}", savedCategory.getId() );
      return mapToResponse( savedCategory );
   }

   @Override
   @Transactional
   public CategoryResponse updateCategory( UpdateCategoryCommand command )
   {
      log.info( "Updating category ID {} with name: {}", command.getCategoryId(), command.getName() );

      // Kategorie laden
      JobCategory category = loadCategoryPort.findById( command.getCategoryId() )
                                             .orElseThrow( () -> CategoryNotFoundException.byId( command.getCategoryId() ) );

      log.debug( "Found category: {}", category.getName() );

      // Duplikat-Check (wenn Name geändert wird)
      if ( !category.getName().equals( command.getName() ) &&
         loadCategoryPort.existsByName( command.getName() ) )
      {
         log.error( "Category already exists with name: {}", command.getName() );
         throw new IllegalArgumentException( "Category already exists with name: " + command.getName() );
      }

      // Kategorie aktualisieren (mit Validierung)
      category.update( command.getName(), command.getDescription() );

      log.debug( "Category details updated" );

      // Kategorie persistieren
      JobCategory updatedCategory = saveCategoryPort.save( category );

      log.info( "Category updated successfully with ID: {}", updatedCategory.getId() );

      return mapToResponse( updatedCategory );
   }

   @Override
   @Transactional
   public void deleteCategory( Long categoryId )
   {
      log.info( "Deleting category ID: {}", categoryId );

      // Kategorie laden (Existenz-Check)
      JobCategory category = loadCategoryPort.findById( categoryId )
                                             .orElseThrow( () -> CategoryNotFoundException.byId( categoryId ) );

      log.debug( "Found category: {}", category.getName() );

      // Prüfe ob Kategorie noch verwendet wird
      long jobCount = loadCategoryPort.countJobsInCategory( categoryId );
      if ( jobCount > 0 )
      {
         log.error( "Cannot delete category: {} jobs still using it", jobCount );
         throw new IllegalStateException(
            String.format( "Cannot delete category: %d job(s) still using it", jobCount )
         );
      }

      // Kategorie löschen
      saveCategoryPort.delete( categoryId );

      log.info( "Category deleted successfully with ID: {}", categoryId );
   }

   @Override
   @Transactional( readOnly = true )
   public CategoryResponse getCategoryById( Long categoryId )
   {
      log.debug( "Fetching category with ID: {}", categoryId );

      JobCategory category = loadCategoryPort.findById( categoryId )
                                             .orElseThrow( () -> CategoryNotFoundException.byId( categoryId ) );

      log.debug( "Found category: {}", category.getName() );

      return mapToResponse( category );
   }

   @Override
   @Transactional( readOnly = true )
   public List<CategoryResponse> getAllCategories()
   {
      log.debug( "Fetching all categories" );

      List<JobCategory> categories = loadCategoryPort.findAll();

      log.debug( "Found {} categories", categories.size() );

      return categories.stream()
                       .map( this :: mapToResponse )
                       .toList();
   }

   private CategoryResponse mapToResponse( JobCategory category )
   {
      // Job-Count laden (für Statistiken)
      long jobCount = loadCategoryPort.countJobsInCategory( category.getId() );

      return CategoryResponse.builder()
                             .id( category.getId() )
                             .name( category.getName() )
                             .description( category.getDescription() )
                             .jobCount( jobCount )
                             .build();
   }
}
