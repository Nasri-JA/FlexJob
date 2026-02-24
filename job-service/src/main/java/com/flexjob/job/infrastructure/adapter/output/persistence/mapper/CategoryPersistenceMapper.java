package com.flexjob.job.infrastructure.adapter.output.persistence.mapper;

import com.flexjob.job.domain.model.JobCategory;
import com.flexjob.job.infrastructure.adapter.output.persistence.entity.JobCategoryJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryPersistenceMapper
{

   public JobCategory toDomain( JobCategoryJpaEntity jpaEntity )
   {
      if ( jpaEntity == null )
      {
         return null;
      }

      return JobCategory.builder()
                        .id( jpaEntity.getId() )
                        .name( jpaEntity.getName() )
                        .description( jpaEntity.getDescription() )
                        .build();
   }

   public JobCategoryJpaEntity toJpaEntity( JobCategory domain )
   {
      if ( domain == null )
      {
         return null;
      }

      return JobCategoryJpaEntity.builder()
                                 .id( domain.getId() )
                                 .name( domain.getName() )
                                 .description( domain.getDescription() )
                                 .build();
   }

   public List<JobCategory> toDomainList( List<JobCategoryJpaEntity> jpaEntities )
   {
      if ( jpaEntities == null )
      {
         return new ArrayList<>();
      }

      return jpaEntities.stream()
                        .map( this :: toDomain )
                        .toList();
   }
}
