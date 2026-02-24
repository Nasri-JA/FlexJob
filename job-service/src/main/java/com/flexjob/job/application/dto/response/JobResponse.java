package com.flexjob.job.application.dto.response;

import com.flexjob.job.enums.JobStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class JobResponse
{
   Long id;
   Long employerId;
   CategoryResponse category;
   String title;
   String description;
   String location;
   Double hourlyRate;
   Integer durationHours;
   JobStatus status;
   List<RequirementResponse> requirements;
   LocalDateTime createdAt;
   LocalDateTime updatedAt;
   Double estimatedTotalCompensation;
   Boolean isPremium;
   Boolean isOpenForApplications;
   Integer qualityScore;

   @Value
   @Builder
   public static class RequirementResponse
   {
      Long id;
      String requirementText;
      Boolean isMandatory;
   }
}
