package com.flexjob.user.infrastructure.messaging;

import com.flexjob.user.shared.EventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher implements EventPublisher
{
   private final KafkaTemplate<String, String> kafkaTemplate;
   private final ObjectMapper objectMapper;
   private static final String TOPIC_PREFIX = "flexjob.user.";

   @Override
   public void publish( Object event, String eventType )
   {
      try
      {
         String eventJson = objectMapper.writeValueAsString( event );

         log.debug( "Publishing event: {} - {}", eventType, eventJson );

         String topic = TOPIC_PREFIX + eventType;

         kafkaTemplate.send( topic, eventJson )
                      .whenComplete( ( result, ex ) -> {
                         if ( ex == null )
                         {
                            log.info( "Event successfully published to Kafka: {} - Topic: {}",
                                      eventType, topic );
                         }
                         else
                         {
                            log.error( "Failed to publish event to Kafka: {} - Topic: {}",
                                       eventType, topic, ex );
                         }
                      } );

      }
      catch ( Exception e )
      {
         log.error( "Failed to serialize event: {}", eventType, e );
         throw new RuntimeException( "Event publishing failed", e );
      }
   }

}
