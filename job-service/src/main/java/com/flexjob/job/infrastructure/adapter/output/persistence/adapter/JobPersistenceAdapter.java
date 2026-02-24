package com.flexjob.job.infrastructure.adapter.output.persistence.adapter;

import com.flexjob.job.application.port.output.LoadJobPort;
import com.flexjob.job.application.port.output.SaveJobPort;
import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.vo.JobId;
import com.flexjob.job.enums.JobStatus;
import com.flexjob.job.infrastructure.adapter.output.persistence.entity.JobJpaEntity;
import com.flexjob.job.infrastructure.adapter.output.persistence.mapper.JobPersistenceMapper;
import com.flexjob.job.infrastructure.adapter.output.persistence.repository.JobJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobPersistenceAdapter implements LoadJobPort, SaveJobPort
{
   private final JobJpaRepository jobJpaRepository;
   private final JobPersistenceMapper jobPersistenceMapper;

   // ========== LoadJobPort Implementation ==========

   @Override
   public Optional<Job> findById( JobId jobId )
   {
      log.debug( "Loading job with ID: {}", jobId.getValue() );

      return jobJpaRepository.findById( jobId.getValue() )
                             .map( jobPersistenceMapper :: toDomain );
   }

   @Override
   public List<Job> findByEmployerId( Long employerId )
   {
      log.debug( "Loading jobs for employer: {}", employerId );

      List<JobJpaEntity> jpaEntities = jobJpaRepository.findByEmployerId( employerId );
      return jobPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public List<Job> findByStatus( JobStatus status )
   {
      log.debug( "Loading jobs with status: {}", status );

      List<JobJpaEntity> jpaEntities = jobJpaRepository.findByStatus( status );
      return jobPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public List<Job> findByCategoryId( Long categoryId )
   {
      log.debug( "Loading jobs in category: {}", categoryId );

      List<JobJpaEntity> jpaEntities = jobJpaRepository.findByCategoryId( categoryId );
      return jobPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public List<Job> findAll()
   {
      log.debug( "Loading all jobs" );

      List<JobJpaEntity> jpaEntities = jobJpaRepository.findAll();
      return jobPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public boolean existsById( JobId jobId )
   {
      log.debug( "Checking if job exists with ID: {}", jobId.getValue() );

      return jobJpaRepository.existsById( jobId.getValue() );
   }

   @Override
   public long countByEmployerId( Long employerId )
   {
      log.debug( "Counting jobs for employer: {}", employerId );

      return jobJpaRepository.countByEmployerId( employerId );
   }

   @Override
   public long countByStatus( JobStatus status )
   {
      log.debug( "Counting jobs with status: {}", status );

      return jobJpaRepository.countByStatus( status );
   }

   // ========== SaveJobPort Implementation ==========

   @Override
   public Job save( Job job )
   {
      log.debug( "Saving job: {}", job.getTitle().getValue() );

      // Domain -> JPA
      JobJpaEntity jpaEntity = jobPersistenceMapper.toJpaEntity( job );

      // Save to DB
      JobJpaEntity savedEntity = jobJpaRepository.save( jpaEntity );

      log.debug( "Job saved with ID: {}", savedEntity.getId() );

      // JPA -> Domain
      return jobPersistenceMapper.toDomain( savedEntity );
   }

   @Override
   public void delete( JobId jobId )
   {
      log.info( "Deleting job with ID: {} (Hard-Delete)", jobId.getValue() );

      jobJpaRepository.deleteById( jobId.getValue() );
   }

   @Override
   public boolean isOwnedByEmployer( JobId jobId, Long employerId )
   {
      log.debug( "Checking ownership: Job {} belongs to Employer {}?", jobId.getValue(), employerId );

      return jobJpaRepository.findById( jobId.getValue() )
                             .map( job -> job.getEmployerId().equals( employerId ) )
                             .orElse( false );
   }
}
