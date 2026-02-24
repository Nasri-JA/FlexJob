package com.flexjob.job.infrastructure.adapter.input.rest.controller;

import com.flexjob.job.application.dto.command.CreateCategoryCommand;
import com.flexjob.job.application.dto.command.UpdateCategoryCommand;
import com.flexjob.job.application.dto.response.CategoryResponse;
import com.flexjob.job.application.port.input.ManageCategoriesUseCase;
import com.flexjob.job.infrastructure.adapter.input.rest.dto.CreateCategoryRequest;
import com.flexjob.job.infrastructure.adapter.input.rest.mapper.CategoryRestMapper;
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
   private final CategoryRestMapper categoryRestMapper;

   @PostMapping
   public ResponseEntity<CategoryResponse> createCategory(
      @Valid @RequestBody CreateCategoryRequest request )
   {

      log.info( "POST /api/categories - Creating category: {}", request.getName() );

      // Map REST DTO -> Command
      CreateCategoryCommand command = categoryRestMapper.toCreateCommand( request );

      // Execute Use Case
      CategoryResponse response = manageCategoriesUseCase.createCategory( command );

      log.info( "Category created successfully with ID: {}", response.getId() );

      return ResponseEntity.status( HttpStatus.CREATED ).body( response );
   }

   @GetMapping( "/{id}" )
   public ResponseEntity<CategoryResponse> getCategoryById( @PathVariable Long id )
   {
      log.info( "GET /api/categories/{} - Fetching category", id );

      // Execute Use Case
      CategoryResponse response = manageCategoriesUseCase.getCategoryById( id );

      return ResponseEntity.ok( response );
   }

   @GetMapping
   public ResponseEntity<List<CategoryResponse>> getAllCategories()
   {
      log.info( "GET /api/categories - Fetching all categories" );

      // Execute Use Case
      List<CategoryResponse> responses = manageCategoriesUseCase.getAllCategories();

      log.info( "Found {} categories", responses.size() );

      return ResponseEntity.ok( responses );
   }

   @PutMapping( "/{id}" )
   public ResponseEntity<CategoryResponse> updateCategory(
      @PathVariable Long id,
      @Valid @RequestBody CreateCategoryRequest request )
   {

      log.info( "PUT /api/categories/{} - Updating category: {}", id, request.getName() );

      // Map REST DTO -> Command
      UpdateCategoryCommand command = categoryRestMapper.toUpdateCommand( id, request );

      // Execute Use Case
      CategoryResponse response = manageCategoriesUseCase.updateCategory( command );

      log.info( "Category updated successfully with ID: {}", response.getId() );

      return ResponseEntity.ok( response );
   }

   @DeleteMapping( "/{id}" )
   public ResponseEntity<Void> deleteCategory( @PathVariable Long id )
   {
      log.info( "DELETE /api/categories/{} - Deleting category", id );

      // Execute Use Case
      manageCategoriesUseCase.deleteCategory( id );

      log.info( "Category deleted successfully with ID: {}", id );

      return ResponseEntity.noContent().build();
   }
}
