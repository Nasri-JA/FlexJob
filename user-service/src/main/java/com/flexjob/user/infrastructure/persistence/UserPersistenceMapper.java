package com.flexjob.user.infrastructure.persistence;

import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.model.UserProfile;
import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper
{
   public User toDomainModel( UserJpaEntity entity )
   {
      if ( entity == null )
      {
         return null;
      }

      return User.builder()
                 .id( UserId.of( entity.getId() ) )
                 .email( Email.of( entity.getEmail() ) )

                 .passwordHash( entity.getPasswordHash() )
                 .userType( entity.getUserType() )
                 .active( entity.isActive() )
                 .emailVerified( entity.isEmailVerified() )
                 .createdAt( entity.getCreatedAt() )
                 .updatedAt( entity.getUpdatedAt() )

                 .profile( toProfileDomainModel( entity.getProfile() ) )
                 .build();
   }

   public UserJpaEntity toJpaEntity( User user )
   {
      if ( user == null )
      {
         return null;
      }

      UserJpaEntity entity = UserJpaEntity.builder()
                                          .id( user.getId().getValue() )
                                          .email( user.getEmail().getValue() )

                                          .passwordHash( user.getPasswordHash() )
                                          .userType( user.getUserType() )
                                          .active( user.isActive() )
                                          .emailVerified( user.isEmailVerified() )
                                          .createdAt( user.getCreatedAt() )
                                          .updatedAt( user.getUpdatedAt() )

                                          .profile( toProfileJpaEntity( user.getProfile(),
                                                                        user.getId().getValue() ) )
                                          .build();

      return entity;
   }

   private UserProfile toProfileDomainModel( UserProfileJpaEntity entity )
   {
      if ( entity == null )
      {
         return null;
      }

      return UserProfile.builder()
                        .firstName( entity.getFirstName() )
                        .lastName( entity.getLastName() )
                        .phoneNumber( entity.getPhoneNumber() )
                        .dateOfBirth( entity.getDateOfBirth() )
                        .address( entity.getAddress() )
                        .city( entity.getCity() )
                        .postalCode( entity.getPostalCode() )
                        .country( entity.getCountry() )
                        .profileImageUrl( entity.getProfileImageUrl() )
                        .bio( entity.getBio() )
                        .linkedInUrl( entity.getLinkedInUrl() )
                        .githubUrl( entity.getGithubUrl() )
                        .websiteUrl( entity.getWebsiteUrl() )
                        .build();
   }

   private UserProfileJpaEntity toProfileJpaEntity( UserProfile profile, String userId )
   {
      if ( profile == null )
      {
         return UserProfileJpaEntity.builder()
                                    .id( userId )
                                    .build();
      }

      return UserProfileJpaEntity.builder()
                                 .id( userId ).firstName( profile.getFirstName() )
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
