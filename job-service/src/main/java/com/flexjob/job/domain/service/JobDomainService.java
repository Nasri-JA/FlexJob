package com.flexjob.job.domain.service;

import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.model.JobCategory;
import com.flexjob.job.domain.model.JobRequirement;
import com.flexjob.job.domain.vo.*;
import com.flexjob.job.enums.JobStatus;

import java.time.LocalDateTime;
import java.util.List;

public class JobDomainService
{

   public Job createJob(
      Long employerId,
      JobCategory category,
      JobTitle title,
      JobDescription description,
      Location location,
      HourlyRate hourlyRate,
      JobDuration duration,
      List<JobRequirement> requirements
   )
   {
      validateEmployerId( employerId );
      if ( category == null || category.getId() == null )
      {
         throw new IllegalArgumentException( "Job category is required" );
      }
      LocalDateTime now = LocalDateTime.now();

      Job job = Job.builder()
                   .id( null ) // ID wird bei Persistierung vergeben
                   .employerId( employerId )
                   .category( category )
                   .title( title )
                   .description( description )
                   .location( location )
                   .hourlyRate( hourlyRate )
                   .duration( duration )
                   .status( JobStatus.OPEN )
                   .createdAt( now )
                   .updatedAt( now )
                   .build();
      if ( requirements != null && !requirements.isEmpty() )
      {
         validateRequirements( requirements );
         for ( JobRequirement requirement : requirements )
         {
            job.addRequirement( requirement );
         }
      }

      return job;
   }

   private void validateEmployerId( Long employerId )
   {
      if ( employerId == null || employerId <= 0 )
      {
         throw new IllegalArgumentException( "Invalid employer ID: " + employerId );
      }
   }

   private void validateRequirements( List<JobRequirement> requirements )
   {
      if ( requirements.size() > 20 )
      {
         throw new IllegalArgumentException(
            "Too many requirements: " + requirements.size() + " (max 20)"
         );
      }

      // Prüfe auf Duplikate
      long distinctCount = requirements.stream()
                                       .map( r -> r.getRequirementText().toLowerCase() )
                                       .distinct()
                                       .count();

      if ( distinctCount < requirements.size() )
      {
         throw new IllegalArgumentException( "Duplicate requirements detected" );
      }
      boolean hasMandatory = requirements.stream()
                                         .anyMatch( JobRequirement :: isMandatory );

      if ( !hasMandatory && !requirements.isEmpty() )
      {
         // Log warning (würde normalerweise über Logger gehen)
      }
   }

   public void updateJobDetails(
      Job job,
      JobCategory category,
      JobTitle title,
      JobDescription description,
      Location location,
      HourlyRate hourlyRate,
      JobDuration duration
   )
   {
      if ( job == null )
      {
         throw new IllegalArgumentException( "Job cannot be null" );
      }

      if ( category == null || category.getId() == null )
      {
         throw new IllegalArgumentException( "Job category is required" );
      }

      // Validiere dass Job editierbar ist
      if ( !job.isEditable() )
      {
         throw new IllegalStateException(
            "Cannot update job with status: " + job.getStatus()
         );
      }
      job.update( title, description, location, hourlyRate, duration, category );
   }

   public boolean isValidStatusTransition( JobStatus currentStatus, JobStatus newStatus )
   {
      if ( currentStatus == newStatus )
      {
         return false; // Keine Änderung
      }
      if ( currentStatus == JobStatus.COMPLETED || currentStatus == JobStatus.CANCELLED )
      {
         return false;
      }

      return switch ( currentStatus )
      {
         case OPEN -> newStatus == JobStatus.IN_PROGRESS || newStatus == JobStatus.CANCELLED;
         case IN_PROGRESS -> newStatus == JobStatus.COMPLETED || newStatus == JobStatus.CANCELLED;
         default -> false;
      };
   }

   public int calculateJobQualityScore( Job job )
   {
      int score = 0;

      // Detaillierte Beschreibung (mindestens 100 Wörter)
      if ( job.getDescription().isDetailed() )
      {
         score += 30;
      }

      // Hat genug Anforderungen
      if ( job.getRequirements().size() >= 3 )
      {
         score += 20;
      }

      // Fairer Stundensatz
      if ( job.getHourlyRate().getValueAsDouble() >= 20.0 )
      {
         score += 20;
      }

      // Premium-Job
      if ( job.isPremium() )
      {
         score += 10;
      }

      // Kategorisiert
      if ( job.getCategory() != null )
      {
         score += 20;
      }

      return Math.min( score, 100 ); // Cap bei 100
   }

   public boolean isAttractiveJob( Job job )
   {
      return calculateJobQualityScore( job ) >= 70
         && job.getHourlyRate().getValueAsDouble() >= 25.0
         && job.isOpenForApplications();
   }

   public double calculateTotalCompensation( Job job )
   {
      return job.getHourlyRate()
                .calculateTotal( job.getDuration().getHours() )
                .doubleValue();
   }

   public boolean areSimilarJobs( Job job1, Job job2 )
   {
      if ( job1 == null || job2 == null )
      {
         return false;
      }

      // Gleicher Employer
      if ( !job1.getEmployerId().equals( job2.getEmployerId() ) )
      {
         return false;
      }

      // Gleicher Titel (Case-Insensitive)
      if ( !job1.getTitle().getValue().equalsIgnoreCase( job2.getTitle().getValue() ) )
      {
         return false;
      }

      // Gleicher Standort
      if ( !job1.getLocation().equals( job2.getLocation() ) )
      {
         return false;
      }

      // Ähnlicher Stundensatz (+/- 10%)
      double rate1 = job1.getHourlyRate().getValueAsDouble();
      double rate2 = job2.getHourlyRate().getValueAsDouble();
      double difference = Math.abs( rate1 - rate2 ) / rate1;

      return difference <= 0.10; // 10% Toleranz
   }
}
