package com.flexjob.user.shared.repository;

public interface EventPublisher
{

   void publish( Object event, String eventType );

   default void publish( Object event )
   {
      String eventType = event.getClass().getSimpleName()
                              .replaceAll( "Event$", "" )
                              .replaceAll( "([a-z])([A-Z])", "$1.$2" )
                              .toLowerCase();
      publish( event, eventType );
   }

}
