package com.flexjob.job.getjob.service;

import com.flexjob.job.domain.exception.JobNotFoundException;
import com.flexjob.job.domain.model.Job;
import com.flexjob.job.domain.vo.JobId;
import com.flexjob.job.getjob.usecase.GetJobUseCase;
import com.flexjob.job.shared.dto.JobResponse;
import com.flexjob.job.shared.mapper.JobResponseMapper;
import com.flexjob.job.shared.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional( readOnly = true )
public class GetJobService implements GetJobUseCase
{

   private final JobRepository jobRepository;
   private final JobResponseMapper jobResponseMapper;

   @Override
   public JobResponse getJobById( Long jobId )
   {
      log.debug( "Fetching job with ID: {}", jobId );

      JobId id = JobId.of( jobId );
      Job job = jobRepository.findById( id )
                             .orElseThrow( () -> JobNotFoundException.byId( jobId ) );

      return jobResponseMapper.toJobResponse( job );
   }

   @Override
   public List<JobResponse> getJobsByEmployerId( Long employerId )
   {
      log.debug( "Fetching jobs for employer: {}", employerId );

      return jobRepository.findByEmployerId( employerId ).stream()
                          .map( jobResponseMapper :: toJobResponse )
                          .toList();
   }

   @Override
   public List<JobResponse> getAllJobs()
   {
      log.debug( "Fetching all jobs" );

      return jobRepository.findAll().stream()
                          .map( jobResponseMapper :: toJobResponse )
                          .toList();
   }
}
