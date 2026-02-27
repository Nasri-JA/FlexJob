package com.flexjob.user.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table( name = "user_profiles" )
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileJpaEntity
{
   @Id
   @Column( name = "id", nullable = false, length = 36 )
   private String id;

   @Column( name = "first_name", length = 100 )
   private String firstName;

   @Column( name = "last_name", length = 100 )
   private String lastName;

   @Column( name = "phone_number", length = 20 )
   private String phoneNumber;

   @Column( name = "date_of_birth" )
   private LocalDate dateOfBirth;

   @Column( name = "address", length = 255 )
   private String address;

   @Column( name = "city", length = 100 )
   private String city;

   @Column( name = "postal_code", length = 20 )
   private String postalCode;

   @Column( name = "country", length = 100 )
   private String country;

   @Column( name = "profile_image_url", length = 500 )
   private String profileImageUrl;

   @Column( name = "bio", columnDefinition = "TEXT" )
   private String bio;

   @Column( name = "linkedin_url", length = 255 )
   private String linkedInUrl;

   @Column( name = "github_url", length = 255 )
   private String githubUrl;

   @Column( name = "website_url", length = 255 )
   private String websiteUrl;
}
