package com.flexjob.user.infrastructure.persistence.repository;

import com.flexjob.user.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, String>
{
   Optional<UserJpaEntity> findByEmail( String email );

   boolean existsByEmail( String email );

}
