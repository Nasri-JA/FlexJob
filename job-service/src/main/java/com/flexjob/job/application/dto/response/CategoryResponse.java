package com.flexjob.job.application.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryResponse
{
   Long id;
   String name;
   String description;
   Long jobCount;
}
