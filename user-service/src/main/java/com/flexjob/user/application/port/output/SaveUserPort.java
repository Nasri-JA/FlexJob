package com.flexjob.user.application.port.output;

import com.flexjob.user.domain.model.User;

public interface SaveUserPort
{
   User save( User user );

   void delete( User user );

}
