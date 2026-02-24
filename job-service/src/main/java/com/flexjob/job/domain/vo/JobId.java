package com.flexjob.job.domain.vo;

import java.util.Objects;

public final class JobId
{
   private final Long value;

   private JobId( Long value )
   {
      // Validierung: Null-Check und positive Zahl
      if ( value == null || value <= 0 )
      {
         throw new IllegalArgumentException( "JobId must be a positive number" );
      }
      this.value = value;
   }

   public static JobId of( Long value )
   {
      return new JobId( value );
   }

   public Long getValue()
   {
      return value;
   }

   @Override
   public boolean equals( Object o )
   {
      if ( this == o )
         return true;
      if ( o == null || getClass() != o.getClass() )
         return false;
      JobId jobId = (JobId) o;
      return Objects.equals( value, jobId.value );
   }

   @Override
   public int hashCode()
   {
      return Objects.hash( value );
   }

   @Override
   public String toString()
   {
      return "JobId{" + value + "}";
   }
}
