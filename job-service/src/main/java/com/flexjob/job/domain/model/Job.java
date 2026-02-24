package com.flexjob.job.domain.model;

import com.flexjob.job.domain.vo.*;
import com.flexjob.job.enums.JobStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class Job
{
   private JobId id;
   private Long employerId;
   private JobCategory category;
   private JobTitle title;
   private JobDescription description;
   private Location location;
   private HourlyRate hourlyRate;
   private JobDuration duration;

   @Builder.Default
   private JobStatus status = JobStatus.OPEN;

   @Builder.Default
   private List<JobRequirement> requirements = new ArrayList<>();

   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   public List<JobRequirement> getRequirements()
   {
      return Collections.unmodifiableList( requirements );
   }

   public void addRequirement( JobRequirement requirement )
   {
      if ( requirement == null )
      {
         throw new IllegalArgumentException( "Requirement cannot be null" );
      }
      if ( requirements.size() >= 20 )
      {
         throw new IllegalStateException( "Cannot add more than 20 requirements to a job" );
      }

      // Prüfe auf Duplikate (gleicher Text)
      boolean isDuplicate = requirements.stream()
                                        .anyMatch( r -> r.getRequirementText()
                                                         .equalsIgnoreCase( requirement.getRequirementText() ) );

      if ( isDuplicate )
      {
         throw new IllegalArgumentException( "Duplicate requirement: " + requirement.getRequirementText() );
      }

      requirements.add( requirement );
      this.updatedAt = LocalDateTime.now();
   }

   public void removeRequirement( JobRequirement requirement )
   {
      if ( requirement == null )
      {
         throw new IllegalArgumentException( "Requirement cannot be null" );
      }

      requirements.remove( requirement );
      this.updatedAt = LocalDateTime.now();
   }

   public void clearRequirements()
   {
      requirements.clear();
      this.updatedAt = LocalDateTime.now();
   }

   public void start()
   {
      if ( status != JobStatus.OPEN )
      {
         throw new IllegalStateException(
            "Cannot start job with status: " + status + " (must be OPEN)"
         );
      }

      this.status = JobStatus.IN_PROGRESS;
      this.updatedAt = LocalDateTime.now();
   }

   public void complete()
   {
      if ( status != JobStatus.IN_PROGRESS )
      {
         throw new IllegalStateException(
            "Cannot complete job with status: " + status + " (must be IN_PROGRESS)"
         );
      }

      this.status = JobStatus.COMPLETED;
      this.updatedAt = LocalDateTime.now();
   }

   public void cancel()
   {
      if ( status == JobStatus.COMPLETED )
      {
         throw new IllegalStateException( "Cannot cancel a completed job" );
      }

      if ( status == JobStatus.CANCELLED )
      {
         throw new IllegalStateException( "Job is already cancelled" );
      }

      this.status = JobStatus.CANCELLED;
      this.updatedAt = LocalDateTime.now();
   }

   public void update( JobTitle title, JobDescription description, Location location,
                       HourlyRate hourlyRate, JobDuration duration, JobCategory category )
   {
      if ( status == JobStatus.COMPLETED || status == JobStatus.CANCELLED )
      {
         throw new IllegalStateException(
            "Cannot update job with status: " + status
         );
      }

      this.title = title;
      this.description = description;
      this.location = location;
      this.hourlyRate = hourlyRate;
      this.duration = duration;
      this.category = category;
      this.updatedAt = LocalDateTime.now();
   }

   public boolean isActive()
   {
      return status == JobStatus.OPEN || status == JobStatus.IN_PROGRESS;
   }

   public boolean isOpenForApplications()
   {
      return status == JobStatus.OPEN;
   }

   public boolean isFinished()
   {
      return status == JobStatus.COMPLETED || status == JobStatus.CANCELLED;
   }

   public boolean isEditable()
   {
      return status == JobStatus.OPEN || status == JobStatus.IN_PROGRESS;
   }

   public int getMandatoryRequirementsCount()
   {
      return (int) requirements.stream()
                               .filter( JobRequirement :: isMandatory )
                               .count();
   }

   public int getOptionalRequirementsCount()
   {
      return (int) requirements.stream()
                               .filter( r -> !r.isMandatory() )
                               .count();
   }

   public boolean isPremium()
   {
      return hourlyRate.getValueAsDouble() >= 50.0;
   }

   public boolean isShortTerm()
   {
      return duration.isShortTerm();
   }

   public boolean isLongTerm()
   {
      return duration.isLongTerm();
   }
}
