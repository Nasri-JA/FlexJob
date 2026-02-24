package com.flexjob.job.application.port.output;

import com.flexjob.job.domain.model.JobCategory;

public interface SaveCategoryPort
{
   JobCategory save( JobCategory category );

   void delete( Long categoryId );
}
