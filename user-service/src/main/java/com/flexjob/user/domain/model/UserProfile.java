package com.flexjob.user.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserProfile
{
   private String firstName;
   private String lastName;
   private String phoneNumber;
   private LocalDate dateOfBirth;
   private String address;
   private String city;
   private String postalCode;
   private String country;
   private String profileImageUrl;
   private String bio;
   private String linkedInUrl;
   private String githubUrl;
   private String websiteUrl;

   public String getFullName()
   {
      if ( firstName == null && lastName == null )
      {
         return "";
      }
      if ( firstName == null )
      {
         return lastName;
      }
      if ( lastName == null )
      {
         return firstName;
      }
      return firstName + " " + lastName;
   }

   public boolean isComplete()
   {
      return firstName != null && !firstName.isEmpty()
         && lastName != null && !lastName.isEmpty()
         && phoneNumber != null && !phoneNumber.isEmpty()
         && address != null && !address.isEmpty();
   }

   public int getCompletionPercentage()
   {
      int totalFields = 12;
      int filledFields = 0;

      if ( firstName != null && !firstName.isEmpty() )
         filledFields++;
      if ( lastName != null && !lastName.isEmpty() )
         filledFields++;
      if ( phoneNumber != null && !phoneNumber.isEmpty() )
         filledFields++;
      if ( dateOfBirth != null )
         filledFields++;
      if ( address != null && !address.isEmpty() )
         filledFields++;
      if ( city != null && !city.isEmpty() )
         filledFields++;
      if ( postalCode != null && !postalCode.isEmpty() )
         filledFields++;
      if ( country != null && !country.isEmpty() )
         filledFields++;
      if ( profileImageUrl != null && !profileImageUrl.isEmpty() )
         filledFields++;
      if ( bio != null && !bio.isEmpty() )
         filledFields++;
      if ( linkedInUrl != null && !linkedInUrl.isEmpty() )
         filledFields++;
      if ( websiteUrl != null && !websiteUrl.isEmpty() )
         filledFields++;

      return (int) ( ( filledFields / (double) totalFields ) * 100 );
   }
}
