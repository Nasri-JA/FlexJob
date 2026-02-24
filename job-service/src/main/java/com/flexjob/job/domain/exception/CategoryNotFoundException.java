package com.flexjob.job.domain.exception;

public class CategoryNotFoundException extends RuntimeException
{

   private final Long categoryId;

   private CategoryNotFoundException( String message, Long categoryId )
   {
      super( message );
      this.categoryId = categoryId;
   }

   public static CategoryNotFoundException byId( Long categoryId )
   {
      String message = String.format( "Category not found with ID: %d", categoryId );
      return new CategoryNotFoundException( message, categoryId );
   }

   public static CategoryNotFoundException byName( String categoryName )
   {
      String message = String.format( "Category not found with name: %s", categoryName );
      return new CategoryNotFoundException( message, null );
   }

   public static CategoryNotFoundException withMessage( String message )
   {
      return new CategoryNotFoundException( message, null );
   }

   public Long getCategoryId()
   {
      return categoryId;
   }

   public boolean hasCategoryId()
   {
      return categoryId != null;
   }
}
