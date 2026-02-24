package com.flexjob.user.infrastructure.adapter.output.persistence.adapter;

import com.flexjob.user.application.port.output.LoadUserPort;
import com.flexjob.user.application.port.output.SaveUserPort;
import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;
import com.flexjob.user.infrastructure.adapter.output.persistence.entity.UserJpaEntity;
import com.flexjob.user.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.flexjob.user.infrastructure.adapter.output.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort
{
   private final UserJpaRepository userRepository;
   private final UserPersistenceMapper mapper;

   @Override
   @Transactional( readOnly = true )
   public Optional<User> loadById( UserId userId )
   {
      log.debug( "Loading user by ID from database: {}", userId.getValue() );

      Optional<UserJpaEntity> entityOptional = userRepository.findById( userId.getValue() );

      return entityOptional.map( entity -> {
         log.debug( "User found, mapping to domain model" );
         return mapper.toDomainModel( entity );
      } );

   }

   @Override
   @Transactional( readOnly = true )
   public Optional<User> loadByEmail( Email email )
   {
      log.debug( "Loading user by email from database: {}", email.getMasked() );

      String emailString = email.getValue();

      Optional<UserJpaEntity> entityOptional = userRepository.findByEmail( emailString );

      return entityOptional.map( entity -> {
         log.debug( "User found for email: {}", email.getMasked() );
         return mapper.toDomainModel( entity );
      } );
   }

   @Override
   @Transactional( readOnly = true )
   public boolean existsByEmail( Email email )
   {
      log.debug( "Checking if email exists: {}", email.getMasked() );

      String emailString = email.getValue();
      boolean exists = userRepository.existsByEmail( emailString );

      log.debug( "Email exists: {}", exists );
      return exists;
   }

   @Override
   @Transactional
   public User save( User user )
   {
      log.info( "Saving user to database: {}", user.getId().getValue() );

      UserJpaEntity entity = mapper.toJpaEntity( user );

      UserJpaEntity savedEntity = userRepository.save( entity );

      log.debug( "User saved successfully, ID: {}", savedEntity.getId() );

      User savedUser = mapper.toDomainModel( savedEntity );

      return savedUser;
   }

   @Override
   @Transactional
   public void delete( User user )
   {
      log.warn( "Hard deleting user from database: {}", user.getId().getValue() );

      String userId = user.getId().getValue();

      if ( !userRepository.existsById( userId ) )
      {
         log.warn( "Cannot delete: User not found: {}", userId );
         return;
      }

      userRepository.deleteById( userId );

      log.info( "User hard deleted: {}", userId );
   }

}
