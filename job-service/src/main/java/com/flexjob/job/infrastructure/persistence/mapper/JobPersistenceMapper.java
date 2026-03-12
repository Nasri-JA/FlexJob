package com.flexjob.job.infrastructure.persistence.mapper;

import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.model.JobCategory;
import com.flexjob.job.domain.model.JobRequirement;
import com.flexjob.job.domain.vo.*;
import com.flexjob.job.infrastructure.persistence.entity.JobCategoryJpaEntity;
import com.flexjob.job.infrastructure.persistence.entity.JobJpaEntity;
import com.flexjob.job.infrastructure.persistence.entity.JobRequirementJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobPersistenceMapper
{
   private final CategoryPersistenceMapper categoryMapper;

   public JobPersistenceMapper( CategoryPersistenceMapper categoryMapper )
   {
      this.categoryMapper = categoryMapper;
   }

   public Job toDomain( JobJpaEntity jpaEntity )
   {
      if ( jpaEntity == null )
      {
         return null;
      }

      JobId jobId = JobId.of( jpaEntity.getId() );
      JobTitle title = JobTitle.of( jpaEntity.getTitle() );
      JobDescription description = JobDescription.of( jpaEntity.getDescription() );
      Location location = Location.of( jpaEntity.getLocation() );
      HourlyRate hourlyRate = HourlyRate.of( jpaEntity.getHourlyRate() );
      JobDuration duration = JobDuration.ofHours( jpaEntity.getDurationHours() );
      JobCategory category = categoryMapper.toDomain( jpaEntity.getCategory() );

      List<JobRequirement> requirements = new ArrayList<>();
      if ( jpaEntity.getRequirements() != null )
      {
         for ( JobRequirementJpaEntity reqJpa : jpaEntity.getRequirements() )
         {
            JobRequirement requirement = JobRequirement.builder()
                                                       .id( reqJpa.getId() )
                                                       .requirementText( reqJpa.getRequirementText() )
                                                       .isMandatory( reqJpa.getIsMandatory() )
                                                       .build();
            requirements.add( requirement );
         }
      }

      return Job.builder()
                .id( jobId )
                .employerId( jpaEntity.getEmployerId() )
                .category( category )
                .title( title )
                .description( description )
                .location( location )
                .hourlyRate( hourlyRate )
                .duration( duration )
                .status( jpaEntity.getStatus() )
                .requirements( requirements )
                .createdAt( jpaEntity.getCreatedAt() )
                .updatedAt( jpaEntity.getUpdatedAt() )
                .build();
   }

   public JobJpaEntity toJpaEntity( Job domain )
   {
      if ( domain == null )
      {
         return null;
      }

      Long id = domain.getId() != null ? domain.getId().getValue() : null;
      String title = domain.getTitle().getValue();
      String description = domain.getDescription().getValue();
      String location = domain.getLocation().getValue();
      Double hourlyRate = domain.getHourlyRate().getValueAsDouble();
      Integer durationHours = domain.getDuration().getHours();
      JobCategoryJpaEntity categoryJpa = categoryMapper.toJpaEntity( domain.getCategory() );

      JobJpaEntity jpaEntity = JobJpaEntity.builder()
                                           .id( id )
                                           .employerId( domain.getEmployerId() )
                                           .category( categoryJpa )
                                           .title( title )
                                           .description( description )
                                           .location( location )
                                           .hourlyRate( hourlyRate )
                                           .durationHours( durationHours )
                                           .status( domain.getStatus() )
                                           .createdAt( domain.getCreatedAt() )
                                           .updatedAt( domain.getUpdatedAt() )
                                           .requirements( new ArrayList<>() )
                                           .build();

      if ( domain.getRequirements() != null )
      {
         for ( JobRequirement reqDomain : domain.getRequirements() )
         {
            JobRequirementJpaEntity reqJpa = JobRequirementJpaEntity.builder()
                                                                    .id( reqDomain.getId() )
                                                                    .requirementText( reqDomain.getRequirementText() )
                                                                    .isMandatory( reqDomain.isMandatory() )
                                                                    .build();
            jpaEntity.addRequirement( reqJpa );
         }
      }

      return jpaEntity;
   }

   public List<Job> toDomainList( List<JobJpaEntity> jpaEntities )
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
