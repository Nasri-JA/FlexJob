package com.flexjob.job.infrastructure.adapter.input.rest.dto;

import com.flexjob.job.enums.JobStatus;
import lombok.Data;

@Data
public class SearchJobsRequest
{
   private JobStatus status;
   private Long categoryId;
   private String location;
   private Double minRate;
   private Double maxRate;
   private Integer minDurationHours;
   private Integer maxDurationHours;
   private String keywords;
}
