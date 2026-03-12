package com.flexjob.job.infrastructure.persistence.adapter;

import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.model.JobStatus;
import com.flexjob.job.domain.vo.JobId;
import com.flexjob.job.infrastructure.persistence.entity.JobJpaEntity;
import com.flexjob.job.infrastructure.persistence.mapper.JobPersistenceMapper;
import com.flexjob.job.infrastructure.persistence.repository.JobJpaRepository;
import com.flexjob.job.shared.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobPersistenceAdapter implements JobRepository
{
   private final JobJpaRepository jobJpaRepository;
   private final JobPersistenceMapper jobPersistenceMapper;

   @Override
   public Optional<Job> findById( JobId jobId )
   {
      return jobJpaRepository.findById( jobId.getValue() )
                             .map( jobPersistenceMapper :: toDomain );
   }

   @Override
   public List<Job> findByEmployerId( Long employerId )
   {
      List<JobJpaEntity> jpaEntities = jobJpaRepository.findByEmployerId( employerId );
      return jobPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public List<Job> findByStatus( JobStatus status )
   {
      List<JobJpaEntity> jpaEntities = jobJpaRepository.findByStatus( status );
      return jobPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public List<Job> findByCategoryId( Long categoryId )
   {
      List<JobJpaEntity> jpaEntities = jobJpaRepository.findByCategoryId( categoryId );
      return jobPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public List<Job> findAll()
   {
      List<JobJpaEntity> jpaEntities = jobJpaRepository.findAll();
      return jobPersistenceMapper.toDomainList( jpaEntities );
   }

   @Override
   public boolean existsById( JobId jobId )
   {
      return jobJpaRepository.existsById( jobId.getValue() );
   }

   @Override
   public long countByEmployerId( Long employerId )
   {
      return jobJpaRepository.countByEmployerId( employerId );
   }

   @Override
   public long countByStatus( JobStatus status )
   {
      return jobJpaRepository.countByStatus( status );
   }

   @Override
   public Job save( Job job )
   {
      JobJpaEntity jpaEntity = jobPersistenceMapper.toJpaEntity( job );
      JobJpaEntity savedEntity = jobJpaRepository.save( jpaEntity );
      return jobPersistenceMapper.toDomain( savedEntity );
   }

   @Override
   public void delete( JobId jobId )
   {
      jobJpaRepository.deleteById( jobId.getValue() );
   }

   @Override
   public boolean isOwnedByEmployer( JobId jobId, Long employerId )
   {
      return jobJpaRepository.findById( jobId.getValue() )
                             .map( job -> job.getEmployerId().equals( employerId ) )
                             .orElse( false );
   }
}
