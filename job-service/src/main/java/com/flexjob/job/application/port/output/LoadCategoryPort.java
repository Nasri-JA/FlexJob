package com.flexjob.job.application.port.output;

import com.flexjob.job.domain.model.JobCategory;

import java.util.List;
import java.util.Optional;

public interface LoadCategoryPort
{
   Optional<JobCategory> findById( Long categoryId );

   Optional<JobCategory> findByName( String name );

   List<JobCategory> findAll();

   boolean existsById( Long categoryId );

   boolean existsByName( String name );

   long countJobsInCategory( Long categoryId );
}
