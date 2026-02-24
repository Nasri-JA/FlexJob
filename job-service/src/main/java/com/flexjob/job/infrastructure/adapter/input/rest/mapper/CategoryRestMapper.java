package com.flexjob.job.infrastructure.adapter.input.rest.mapper;

import com.flexjob.job.application.dto.command.CreateCategoryCommand;
import com.flexjob.job.application.dto.command.UpdateCategoryCommand;
import com.flexjob.job.infrastructure.adapter.input.rest.dto.CreateCategoryRequest;
import org.springframework.stereotype.Component;

@Component
public class CategoryRestMapper
{

   public CreateCategoryCommand toCreateCommand( CreateCategoryRequest request )
   {
      return CreateCategoryCommand.builder()
                                  .name( request.getName() )
                                  .description( request.getDescription() )
                                  .build();
   }

   public UpdateCategoryCommand toUpdateCommand( Long categoryId, CreateCategoryRequest request )
   {
      return UpdateCategoryCommand.builder()
                                  .categoryId( categoryId )
                                  .name( request.getName() )
                                  .description( request.getDescription() )
                                  .build();
   }
}
