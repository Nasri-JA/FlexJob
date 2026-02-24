package com.flexjob.job.application.port.output;

import com.flexjob.job.domain.model.Job;

public interface PublishEventPort
{
   void publishJobCreated( Job job );

   void publishJobUpdated( Job job );

   void publishJobCancelled( Job job );

   void publishJobCompleted( Job job );
}
