package com.flexjob.user.domain.model;

public enum UserType
{
   CANDIDATE( "Kandidat" ),
   EMPLOYER( "Arbeitgeber" ),
   ADMIN( "Administrator" );
   private final String displayName;

   UserType( String displayName )
   {
      this.displayName = displayName;
   }

   public String getDisplayName()
   {
      return displayName;
   }

   public boolean isCandidate()
   {
      return this == CANDIDATE;
   }

   public boolean isEmployer()
   {
      return this == EMPLOYER;
   }

   public boolean isAdmin()
   {
      return this == ADMIN;
   }

   public String getRole()
   {
      return "ROLE_" + this.name();
   }
}
