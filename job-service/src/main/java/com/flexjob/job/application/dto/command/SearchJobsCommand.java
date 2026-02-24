package com.flexjob.job.application.dto.command;

import com.flexjob.job.enums.JobStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SearchJobsCommand
{
   JobStatus status;
   Long categoryId;
   String location;
   Double minRate;
   Double maxRate;
   Integer minDurationHours;
   Integer maxDurationHours;
   String keywords;
   Long employerId;
}
