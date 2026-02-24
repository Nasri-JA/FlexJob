package com.flexjob.job.infrastructure.adapter.output.persistence.adapter;

import com.flexjob.job.application.port.output.LoadCategoryPort;
import com.flexjob.job.application.port.output.SaveCategoryPort;
import com.flexjob.job.domain.model.JobCategory;
import com.flexjob.job.infrastructure.adapter.output.persistence.entity.JobCategoryJpaEntity;
import com.flexjob.job.infrastructure.adapter.output.persistence.mapper.CategoryPersistenceMapper;
import com.flexjob.job.infrastructure.adapter.output.persistence.repository.JobCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryPersistenceAdapter implements LoadCategoryPort, SaveCategoryPort
{
   private final JobCategoryJpaRepository categoryJpaRepository;
   private final CategoryPersistenceMapper categoryPersistenceMapper;

   // ========== LoadCategoryPort Implementation ==========

   @Override
   public Optional<JobCategory> findById( Long categoryId )
   {
      log.debug( "Loading category with ID: {}", categoryId );

      return categoryJpaRepository.findById( categoryId )
                                  .map( categoryPersistenceMapper :: toDomain );
   }

   @Override
   public Optional<JobCategory> findByName( String name )
   {
      log.debug( "Loading category with name: {}", name );

      return categoryJpaRepository.findByName( name )
                                  .map( categoryPersistenceMapper :: toDomain );
   }

   @Override
   public List<JobCategory> findAll()
   {
      log.debug( "Loading all categories" );

      List<JobCategoryJpaEntity> jpaEntities = categoryJpaRepository.findAll();
      return categoryPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public boolean existsById( Long categoryId )
   {
      log.debug( "Checking if category exists with ID: {}", categoryId );

      return categoryJpaRepository.existsById( categoryId );
   }

   @Override
   public boolean existsByName( String name )
   {
      log.debug( "Checking if category exists with name: {}", name );

      return categoryJpaRepository.existsByName( name );
   }

   @Override
   public long countJobsInCategory( Long categoryId )
   {
      log.debug( "Counting jobs in category: {}", categoryId );

      return categoryJpaRepository.countJobsInCategory( categoryId );
   }

   // ========== SaveCategoryPort Implementation ==========

   @Override
   public JobCategory save( JobCategory category )
   {
      log.debug( "Saving category: {}", category.getName() );

      // Domain -> JPA
      JobCategoryJpaEntity jpaEntity = categoryPersistenceMapper.toJpaEntity( category );

      // Save to DB
      JobCategoryJpaEntity savedEntity = categoryJpaRepository.save( jpaEntity );

      log.debug( "Category saved with ID: {}", savedEntity.getId() );

      // JPA -> Domain
      return categoryPersistenceMapper.toDomain( savedEntity );
   }

   @Override
   public void delete( Long categoryId )
   {
      log.info( "Deleting category with ID: {}", categoryId );
      long jobCount = countJobsInCategory( categoryId );
      if ( jobCount > 0 )
      {
         throw new IllegalStateException(
            String.format( "Cannot delete category: %d job(s) still using it", jobCount )
         );
      }

      categoryJpaRepository.deleteById( categoryId );
   }
}
