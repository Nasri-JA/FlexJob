package com.flexjob.user.application.port.output;

import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;

import java.util.Optional;

public interface LoadUserPort
{
   Optional<User> loadById( UserId userId );

   Optional<User> loadByEmail( Email email );

   boolean existsByEmail( Email email );

}
