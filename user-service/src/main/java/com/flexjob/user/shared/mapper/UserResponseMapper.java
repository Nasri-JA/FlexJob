package com.flexjob.user.shared.mapper;

import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.model.UserProfile;
import com.flexjob.user.shared.dto.ProfileResponse;
import com.flexjob.user.shared.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper
{
   public UserResponse toUserResponse( User user )
   {
      return UserResponse.builder()
                         .id( user.getId().getValue() )
                         .email( user.getEmail().getValue() )
                         .userType( user.getUserType() )
                         .active( user.isActive() )
                         .emailVerified( user.isEmailVerified() )
                         .createdAt( user.getCreatedAt() )
                         .updatedAt( user.getUpdatedAt() )
                         .profile( toProfileResponse( user.getProfile() ) )
                         .build();
   }

   private ProfileResponse toProfileResponse( UserProfile profile )
   {
      if ( profile == null )
      {
         return null;
      }

      return ProfileResponse.builder()
                            .firstName( profile.getFirstName() )
                            .lastName( profile.getLastName() )
                            .phoneNumber( profile.getPhoneNumber() )
                            .dateOfBirth( profile.getDateOfBirth() )
                            .address( profile.getAddress() )
                            .city( profile.getCity() )
                            .postalCode( profile.getPostalCode() )
                            .country( profile.getCountry() )
                            .profileImageUrl( profile.getProfileImageUrl() )
                            .bio( profile.getBio() )
                            .linkedInUrl( profile.getLinkedInUrl() )
                            .githubUrl( profile.getGithubUrl() )
                            .websiteUrl( profile.getWebsiteUrl() )
                            .build();
   }
}
