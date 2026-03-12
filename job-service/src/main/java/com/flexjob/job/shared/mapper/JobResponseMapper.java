package com.flexjob.job.shared.mapper;

import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.model.JobCategory;
import com.flexjob.job.domain.service.JobDomainService;
import com.flexjob.job.shared.dto.CategoryResponse;
import com.flexjob.job.shared.dto.JobResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JobResponseMapper
{
   private final JobDomainService jobDomainService;

   public JobResponse toJobResponse( Job job )
   {
      List<JobResponse.RequirementResponse> requirementResponses = job.getRequirements().stream()
                                                                      .map( req -> JobResponse.RequirementResponse.builder()
                                                                                                                  .id( req.getId() )
                                                                                                                  .requirementText( req.getRequirementText() )
                                                                                                                  .isMandatory( req.isMandatory() )
                                                                                                                  .build() )
                                                                      .toList();

      CategoryResponse categoryResponse = toCategoryResponse( job.getCategory() );

      double totalCompensation = jobDomainService.calculateTotalCompensation( job );
      int qualityScore = jobDomainService.calculateJobQualityScore( job );

      return JobResponse.builder()
                        .id( job.getId().getValue() )
                        .employerId( job.getEmployerId() )
                        .category( categoryResponse )
                        .title( job.getTitle().getValue() )
                        .description( job.getDescription().getValue() )
                        .location( job.getLocation().getValue() )
                        .hourlyRate( job.getHourlyRate().getValueAsDouble() )
                        .durationHours( job.getDuration().getHours() )
                        .status( job.getStatus() )
                        .requirements( requirementResponses )
                        .createdAt( job.getCreatedAt() )
                        .updatedAt( job.getUpdatedAt() )
                        .estimatedTotalCompensation( totalCompensation )
                        .isPremium( job.isPremium() )
                        .isOpenForApplications( job.isOpenForApplications() )
                        .qualityScore( qualityScore )
                        .build();
   }

   public CategoryResponse toCategoryResponse( JobCategory category )
   {
      if ( category == null )
      {
         return null;
      }

      return CategoryResponse.builder()
                             .id( category.getId() )
                             .name( category.getName() )
                             .description( category.getDescription() )
                             .build();
   }
}
