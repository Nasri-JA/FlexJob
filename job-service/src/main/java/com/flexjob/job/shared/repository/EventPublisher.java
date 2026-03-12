package com.flexjob.job.shared.repository;

import com.flexjob.job.domain.model.Job;

public interface EventPublisher
{
   void publishJobCreated( Job job );

   void publishJobUpdated( Job job );

   void publishJobCancelled( Job job );

   void publishJobCompleted( Job job );
}
