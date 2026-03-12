package com.flexjob.job.shared.dto;

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
