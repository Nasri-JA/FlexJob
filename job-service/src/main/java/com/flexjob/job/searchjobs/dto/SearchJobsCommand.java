package com.flexjob.job.searchjobs.dto;

import com.flexjob.job.domain.model.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchJobsCommand
{
   private JobStatus status;
   private Long categoryId;
   private String location;
   private Double minRate;
   private Double maxRate;
   private Integer minDurationHours;
   private Integer maxDurationHours;
   private String keywords;
   private Long employerId;
}
