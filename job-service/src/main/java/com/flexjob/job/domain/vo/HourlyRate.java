package com.flexjob.job.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class HourlyRate
{
   private static final BigDecimal MIN_HOURLY_RATE = new BigDecimal( "10.00" );
   private static final BigDecimal MAX_HOURLY_RATE = new BigDecimal( "1000.00" );
   private static final int SCALE = 2;

   private final BigDecimal value;

   private HourlyRate( BigDecimal value )
   {
      if ( value == null )
      {
         throw new IllegalArgumentException( "Hourly rate cannot be null" );
      }
      BigDecimal rounded = value.setScale( SCALE, RoundingMode.HALF_UP );
      if ( rounded.compareTo( MIN_HOURLY_RATE ) < 0 )
      {
         throw new IllegalArgumentException(
            String.format( "Hourly rate must be at least %.2f EUR (got: %.2f)",
                           MIN_HOURLY_RATE, rounded )
         );
      }
      if ( rounded.compareTo( MAX_HOURLY_RATE ) > 0 )
      {
         throw new IllegalArgumentException(
            String.format( "Hourly rate cannot exceed %.2f EUR (got: %.2f)",
                           MAX_HOURLY_RATE, rounded )
         );
      }

      this.value = rounded;
   }

   public static HourlyRate of( BigDecimal value )
   {
      return new HourlyRate( value );
   }

   public static HourlyRate of( Double value )
   {
      if ( value == null )
      {
         throw new IllegalArgumentException( "Hourly rate cannot be null" );
      }
      return new HourlyRate( BigDecimal.valueOf( value ) );
   }

   public static HourlyRate of( String value )
   {
      if ( value == null || value.trim().isEmpty() )
      {
         throw new IllegalArgumentException( "Hourly rate string cannot be null or empty" );
      }
      try
      {
         return new HourlyRate( new BigDecimal( value.trim() ) );
      }
      catch ( NumberFormatException e )
      {
         throw new IllegalArgumentException( "Invalid hourly rate format: " + value, e );
      }
   }

   public BigDecimal getValue()
   {
      return value;
   }

   public Double getValueAsDouble()
   {
      return value.doubleValue();
   }

   public String getFormatted()
   {
      return String.format( "%.2f EUR", value );
   }

   public BigDecimal calculateTotal( int hours )
   {
      if ( hours < 0 )
      {
         throw new IllegalArgumentException( "Hours cannot be negative" );
      }
      return value.multiply( BigDecimal.valueOf( hours ) );
   }

   public boolean isAbove( BigDecimal threshold )
   {
      return value.compareTo( threshold ) > 0;
   }

   public boolean isBelow( BigDecimal threshold )
   {
      return value.compareTo( threshold ) < 0;
   }

   @Override
   public boolean equals( Object o )
   {
      if ( this == o )
         return true;
      if ( o == null || getClass() != o.getClass() )
         return false;
      HourlyRate that = (HourlyRate) o;
      return value.compareTo( that.value ) == 0;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash( value.doubleValue() );
   }

   @Override
   public String toString()
   {
      return "HourlyRate{" + getFormatted() + "}";
   }
}
