package com.flexjob.job.application.dto.command;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UpdateJobCommand
{
   Long jobId;
   Long employerId;
   Long categoryId;
   String title;
   String description;
   String location;
   Double hourlyRate;
   Integer durationHours;
   List<CreateJobCommand.RequirementCommand> requirements;
}
