package com.flexjob.job.domain.vo;

import java.util.Objects;

public final class JobDuration
{
   private static final int MIN_HOURS = 1;
   private static final int MAX_HOURS = 2000;
   private static final int HOURS_PER_DAY = 8;
   private static final int HOURS_PER_WEEK = 40;

   private final Integer value;

   private JobDuration( Integer value )
   {
      if ( value == null )
      {
         throw new IllegalArgumentException( "Job duration cannot be null" );
      }
      if ( value < MIN_HOURS )
      {
         throw new IllegalArgumentException(
            String.format( "Job duration must be at least %d hour(s), got: %d", MIN_HOURS, value )
         );
      }
      if ( value > MAX_HOURS )
      {
         throw new IllegalArgumentException(
            String.format( "Job duration cannot exceed %d hours (~1 year), got: %d", MAX_HOURS, value )
         );
      }

      this.value = value;
   }

   public static JobDuration ofHours( Integer hours )
   {
      return new JobDuration( hours );
   }

   public static JobDuration ofDays( Integer days )
   {
      if ( days == null || days <= 0 )
      {
         throw new IllegalArgumentException( "Days must be positive" );
      }
      return new JobDuration( days * HOURS_PER_DAY );
   }

   public static JobDuration ofWeeks( Integer weeks )
   {
      if ( weeks == null || weeks <= 0 )
      {
         throw new IllegalArgumentException( "Weeks must be positive" );
      }
      return new JobDuration( weeks * HOURS_PER_WEEK );
   }

   public Integer getHours()
   {
      return value;
   }

   public Integer getDays()
   {
      return (int) Math.ceil( (double) value / HOURS_PER_DAY );
   }

   public Integer getWeeks()
   {
      return (int) Math.ceil( (double) value / HOURS_PER_WEEK );
   }

   public String getFormatted()
   {
      if ( value < HOURS_PER_DAY )
      {
         return value + " hour" + ( value == 1 ? "" : "s" );
      }
      else if ( value < HOURS_PER_WEEK )
      {
         return String.format( "%d hours (~%d day%s)",
                               value, getDays(), getDays() == 1 ? "" : "s" );
      }
      else
      {
         return String.format( "%d hours (~%d day%s, ~%d week%s)",
                               value, getDays(), getDays() == 1 ? "" : "s",
                               getWeeks(), getWeeks() == 1 ? "" : "s" );
      }
   }

   public boolean isShortTerm()
   {
      return value < HOURS_PER_DAY;
   }

   public boolean isMediumTerm()
   {
      return value >= HOURS_PER_DAY && value <= ( 5 * HOURS_PER_DAY );
   }

   public boolean isLongTerm()
   {
      return value > ( 5 * HOURS_PER_DAY );
   }

   @Override
   public boolean equals( Object o )
   {
      if ( this == o )
         return true;
      if ( o == null || getClass() != o.getClass() )
         return false;
      JobDuration that = (JobDuration) o;
      return Objects.equals( value, that.value );
   }

   @Override
   public int hashCode()
   {
      return Objects.hash( value );
   }

   @Override
   public String toString()
   {
      return "JobDuration{" + getFormatted() + "}";
   }
}
