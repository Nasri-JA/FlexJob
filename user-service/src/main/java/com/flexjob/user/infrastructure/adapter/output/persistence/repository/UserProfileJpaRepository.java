package com.flexjob.user.infrastructure.adapter.output.persistence.repository;

import com.flexjob.user.infrastructure.adapter.output.persistence.entity.UserProfileJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfileJpaEntity, String>
{

}
