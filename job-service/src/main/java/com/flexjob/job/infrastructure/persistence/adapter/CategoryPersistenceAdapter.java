package com.flexjob.job.infrastructure.persistence.adapter;

import com.flexjob.job.domain.model.JobCategory;
import com.flexjob.job.infrastructure.persistence.entity.JobCategoryJpaEntity;
import com.flexjob.job.infrastructure.persistence.mapper.CategoryPersistenceMapper;
import com.flexjob.job.infrastructure.persistence.repository.JobCategoryJpaRepository;
import com.flexjob.job.shared.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryPersistenceAdapter implements CategoryRepository
{
   private final JobCategoryJpaRepository categoryJpaRepository;
   private final CategoryPersistenceMapper categoryPersistenceMapper;

   @Override
   public Optional<JobCategory> findById( Long categoryId )
   {
      return categoryJpaRepository.findById( categoryId )
                                  .map( categoryPersistenceMapper :: toDomain );
   }

   @Override
   public Optional<JobCategory> findByName( String name )
   {
      return categoryJpaRepository.findByName( name )
                                  .map( categoryPersistenceMapper :: toDomain );
   }

   @Override
   public List<JobCategory> findAll()
   {
      List<JobCategoryJpaEntity> jpaEntities = categoryJpaRepository.findAll();
      return categoryPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public boolean existsById( Long categoryId )
   {
      return categoryJpaRepository.existsById( categoryId );
   }

   @Override
   public boolean existsByName( String name )
   {
      return categoryJpaRepository.existsByName( name );
   }

   @Override
   public long countJobsInCategory( Long categoryId )
   {
      return categoryJpaRepository.countJobsInCategory( categoryId );
   }

   @Override
   public JobCategory save( JobCategory category )
   {
      JobCategoryJpaEntity jpaEntity = categoryPersistenceMapper.toJpaEntity( category );
      JobCategoryJpaEntity savedEntity = categoryJpaRepository.save( jpaEntity );
      return categoryPersistenceMapper.toDomain( savedEntity );
   }

   @Override
   public void delete( Long categoryId )
   {
      categoryJpaRepository.deleteById( categoryId );
   }
}
