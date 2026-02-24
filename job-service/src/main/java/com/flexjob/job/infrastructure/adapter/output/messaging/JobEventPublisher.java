package com.flexjob.job.infrastructure.adapter.output.messaging;

import com.flexjob.common.events.JobCreatedEvent;
import com.flexjob.job.application.port.output.PublishEventPort;
import com.flexjob.job.domain.model.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobEventPublisher implements PublishEventPort
{
   private final KafkaTemplate<String, JobCreatedEvent> kafkaTemplate;

   private static final String JOB_CREATED_TOPIC = "job-created";
   private static final String JOB_UPDATED_TOPIC = "job-updated";
   private static final String JOB_CANCELLED_TOPIC = "job-cancelled";
   private static final String JOB_COMPLETED_TOPIC = "job-completed";

   @Override
   public void publishJobCreated( Job job )
   {
      try
      {
         log.debug( "Publishing JobCreated event for job ID: {}", job.getId().getValue() );

         // Build Event from Domain
         JobCreatedEvent event = JobCreatedEvent.builder()
                                                .jobId( job.getId().getValue() )
                                                .employerId( job.getEmployerId() )
                                                .title( job.getTitle().getValue() )
                                                .location( job.getLocation().getValue() )
                                                .hourlyRate( job.getHourlyRate().getValueAsDouble() )
                                                .createdAt( job.getCreatedAt() )
                                                .build();

         // Send to Kafka
         kafkaTemplate.send( JOB_CREATED_TOPIC, String.valueOf( job.getId().getValue() ), event );

         log.info( "Published JobCreated event for job ID: {}", job.getId().getValue() );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish JobCreated event for job ID: {}",
                    job.getId().getValue(), e );
         throw e;
      }
   }

   @Override
   public void publishJobUpdated( Job job )
   {
      try
      {
         log.debug( "Publishing JobUpdated event for job ID: {}", job.getId().getValue() );

         // Build Event (reuse JobCreatedEvent structure)
         JobCreatedEvent event = JobCreatedEvent.builder()
                                                .jobId( job.getId().getValue() )
                                                .employerId( job.getEmployerId() )
                                                .title( job.getTitle().getValue() )
                                                .location( job.getLocation().getValue() )
                                                .hourlyRate( job.getHourlyRate().getValueAsDouble() )
                                                .createdAt( job.getUpdatedAt() ) // Use updatedAt for update events
                                                .build();

         kafkaTemplate.send( JOB_UPDATED_TOPIC, String.valueOf( job.getId().getValue() ), event );

         log.info( "Published JobUpdated event for job ID: {}", job.getId().getValue() );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish JobUpdated event for job ID: {}",
                    job.getId().getValue(), e );
         throw e;
      }
   }

   @Override
   public void publishJobCancelled( Job job )
   {
      try
      {
         log.debug( "Publishing JobCancelled event for job ID: {}", job.getId().getValue() );

         // Build Event
         JobCreatedEvent event = JobCreatedEvent.builder()
                                                .jobId( job.getId().getValue() )
                                                .employerId( job.getEmployerId() )
                                                .title( job.getTitle().getValue() )
                                                .location( job.getLocation().getValue() )
                                                .hourlyRate( job.getHourlyRate().getValueAsDouble() )
                                                .createdAt( job.getUpdatedAt() )
                                                .build();

         kafkaTemplate.send( JOB_CANCELLED_TOPIC, String.valueOf( job.getId().getValue() ), event );

         log.info( "Published JobCancelled event for job ID: {}", job.getId().getValue() );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish JobCancelled event for job ID: {}",
                    job.getId().getValue(), e );
         throw e;
      }
   }

   @Override
   public void publishJobCompleted( Job job )
   {
      try
      {
         log.debug( "Publishing JobCompleted event for job ID: {}", job.getId().getValue() );

         // Build Event
         JobCreatedEvent event = JobCreatedEvent.builder()
                                                .jobId( job.getId().getValue() )
                                                .employerId( job.getEmployerId() )
                                                .title( job.getTitle().getValue() )
                                                .location( job.getLocation().getValue() )
                                                .hourlyRate( job.getHourlyRate().getValueAsDouble() )
                                                .createdAt( job.getUpdatedAt() )
                                                .build();

         kafkaTemplate.send( JOB_COMPLETED_TOPIC, String.valueOf( job.getId().getValue() ), event );

         log.info( "Published JobCompleted event for job ID: {}", job.getId().getValue() );
      }
      catch ( Exception e )
      {
         log.error( "Failed to publish JobCompleted event for job ID: {}",
                    job.getId().getValue(), e );
         throw e;
      }
   }
}
