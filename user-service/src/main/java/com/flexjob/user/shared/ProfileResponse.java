package com.flexjob.user.shared;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ProfileResponse
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
}
