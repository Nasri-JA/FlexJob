package com.flexjob.job.application.dto.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateCategoryCommand
{
   Long categoryId;
   String name;
   String description;
}
