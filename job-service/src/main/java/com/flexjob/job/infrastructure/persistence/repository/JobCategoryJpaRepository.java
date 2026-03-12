package com.flexjob.job.infrastructure.persistence.repository;

import com.flexjob.job.infrastructure.persistence.entity.JobCategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobCategoryJpaRepository extends JpaRepository<JobCategoryJpaEntity, Long>
{
   Optional<JobCategoryJpaEntity> findByName( String name );

   boolean existsByName( String name );

   @Query( "SELECT COUNT(j) FROM JobJpaEntity j WHERE j.category.id = :categoryId" )
   long countJobsInCategory( @Param( "categoryId" ) Long categoryId );
}
