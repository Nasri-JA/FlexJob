package com.flexjob.job.category.controller;

import com.flexjob.job.category.dto.CreateCategoryCommand;
import com.flexjob.job.category.dto.UpdateCategoryCommand;
import com.flexjob.job.category.usecase.ManageCategoriesUseCase;
import com.flexjob.job.shared.dto.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/categories" )
@RequiredArgsConstructor
@Slf4j
public class CategoryController
{
   private final ManageCategoriesUseCase manageCategoriesUseCase;

   @PostMapping
   public ResponseEntity<CategoryResponse> createCategory(
      @Valid @RequestBody CreateCategoryCommand command )
   {
      log.info( "POST /api/categories - Creating category: {}", command.getName() );

      CategoryResponse response = manageCategoriesUseCase.createCategory( command );

      return ResponseEntity.status( HttpStatus.CREATED ).body( response );
   }

   @GetMapping( "/{id}" )
   public ResponseEntity<CategoryResponse> getCategoryById( @PathVariable Long id )
   {
      log.info( "GET /api/categories/{} - Fetching category", id );

      CategoryResponse response = manageCategoriesUseCase.getCategoryById( id );

      return ResponseEntity.ok( response );
   }

   @GetMapping
   public ResponseEntity<List<CategoryResponse>> getAllCategories()
   {
      log.info( "GET /api/categories - Fetching all categories" );

      List<CategoryResponse> responses = manageCategoriesUseCase.getAllCategories();

      return ResponseEntity.ok( responses );
   }

   @PutMapping( "/{id}" )
   public ResponseEntity<CategoryResponse> updateCategory(
      @PathVariable Long id,
      @Valid @RequestBody UpdateCategoryCommand command )
   {
      log.info( "PUT /api/categories/{} - Updating category", id );

      command.setCategoryId( id );
      CategoryResponse response = manageCategoriesUseCase.updateCategory( command );

      return ResponseEntity.ok( response );
   }

   @DeleteMapping( "/{id}" )
   public ResponseEntity<Void> deleteCategory( @PathVariable Long id )
   {
      log.info( "DELETE /api/categories/{} - Deleting category", id );

      manageCategoriesUseCase.deleteCategory( id );

      return ResponseEntity.noContent().build();
   }
}
