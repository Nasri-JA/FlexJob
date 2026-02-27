package com.flexjob.user.shared;

import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;

import java.util.Optional;

public interface UserRepository
{
   Optional<User> loadById( UserId userId );

   Optional<User> loadByEmail( Email email );

   boolean existsByEmail( Email email );

   User save( User user );

   void delete( User user );

}
