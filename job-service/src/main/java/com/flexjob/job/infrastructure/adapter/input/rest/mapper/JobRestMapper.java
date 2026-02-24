package com.flexjob.job.infrastructure.adapter.input.rest.mapper;

import com.flexjob.job.application.dto.command.CreateJobCommand;
import com.flexjob.job.application.dto.command.SearchJobsCommand;
import com.flexjob.job.application.dto.command.UpdateJobCommand;
import com.flexjob.job.infrastructure.adapter.input.rest.dto.CreateJobRequest;
import com.flexjob.job.infrastructure.adapter.input.rest.dto.SearchJobsRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class JobRestMapper
{

   public CreateJobCommand toCreateCommand( CreateJobRequest request, Long employerId )
   {
      return CreateJobCommand.builder()
                             .employerId( employerId )
                             .categoryId( request.getCategoryId() )
                             .title( request.getTitle() )
                             .description( request.getDescription() )
                             .location( request.getLocation() )
                             .hourlyRate( request.getHourlyRate() )
                             .durationHours( request.getDurationHours() )
                             .requirements( request.getRequirements() != null
                                            ? request.getRequirements().stream()
                                                     .map( req -> CreateJobCommand.RequirementCommand.builder()
                                                                                                     .requirementText(
                                                                                                        req.getRequirementText() )
                                                                                                     .isMandatory(
                                                                                                        req.getIsMandatory() )
                                                                                                     .build() )
                                                     .collect( Collectors.toList() )
                                            : null )
                             .build();
   }

   public UpdateJobCommand toUpdateCommand( Long jobId, CreateJobRequest request, Long employerId )
   {
      return UpdateJobCommand.builder()
                             .jobId( jobId )
                             .employerId( employerId )
                             .categoryId( request.getCategoryId() )
                             .title( request.getTitle() )
                             .description( request.getDescription() )
                             .location( request.getLocation() )
                             .hourlyRate( request.getHourlyRate() )
                             .durationHours( request.getDurationHours() )
                             .requirements( request.getRequirements() != null
                                            ? request.getRequirements().stream()
                                                     .map( req -> CreateJobCommand.RequirementCommand.builder()
                                                                                                     .requirementText(
                                                                                                        req.getRequirementText() )
                                                                                                     .isMandatory(
                                                                                                        req.getIsMandatory() )
                                                                                                     .build() )
                                                     .collect( Collectors.toList() )
                                            : null )
                             .build();
   }

   public SearchJobsCommand toSearchCommand( SearchJobsRequest request )
   {
      return SearchJobsCommand.builder()
                              .status( request.getStatus() )
                              .categoryId( request.getCategoryId() )
                              .location( request.getLocation() )
                              .minRate( request.getMinRate() )
                              .maxRate( request.getMaxRate() )
                              .minDurationHours( request.getMinDurationHours() )
                              .maxDurationHours( request.getMaxDurationHours() )
                              .keywords( request.getKeywords() )
                              .build();
   }
}
