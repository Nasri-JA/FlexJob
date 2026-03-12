package com.flexjob.job.infrastructure.messaging;

import com.flexjob.common.events.JobCreatedEvent;
import com.flexjob.job.domain.model.Job;
import com.flexjob.job.shared.repository.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobEventPublisher implements EventPublisher
{
   private final KafkaTemplate<String, JobCreatedEvent> kafkaTemplate;

   private static final String JOB_CREATED_TOPIC = "job-created";
   private static final String JOB_UPDATED_TOPIC = "job-updated";
   private static final String JOB_CANCELLED_TOPIC = "job-cancelled";
   private static final String JOB_COMPLETED_TOPIC = "job-completed";

   @Override
   public void publishJobCreated( Job job )
   {
      sendEvent( JOB_CREATED_TOPIC, job );
   }

   @Override
   public void publishJobUpdated( Job job )
   {
      sendEvent( JOB_UPDATED_TOPIC, job );
   }

   @Override
   public void publishJobCancelled( Job job )
   {
      sendEvent( JOB_CANCELLED_TOPIC, job );
   }

   @Override
   public void publishJobCompleted( Job job )
   {
      sendEvent( JOB_COMPLETED_TOPIC, job );
   }

   private void sendEvent( String topic, Job job )
   {
      try
      {
         JobCreatedEvent event = JobCreatedEvent.builder()
                                                .jobId( job.getId().getValue() )
                                                .employerId( job.getEmployerId() )
                                                .title( job.getTitle().getValue() )
                                                .location( job.getLocation().getValue() )
                                                .hourlyRate( job.getHourlyRate().getValueAsDouble() )
                                                .createdAt( job.getCreatedAt() )
                                                .build();

         kafkaTemplate.send( topic, String.valueOf( job.getId().getValue() ), event );

         log.info( "Published {} event for job ID: {}", topic, job.getId().getValue() );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish {} event for job ID: {}", topic, job.getId().getValue(), e );
         throw e;
      }
   }
}
