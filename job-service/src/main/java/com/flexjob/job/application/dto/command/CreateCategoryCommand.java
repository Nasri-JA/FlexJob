package com.flexjob.job.application.dto.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCategoryCommand
{
   String name;
   String description;
}
