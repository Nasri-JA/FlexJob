package com.flexjob.user.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginCommand
{
   @NotBlank( message = "E-Mail ist erforderlich" )
   @Email( message = "Ungültiges E-Mail-Format" )
   private String email;

   @NotBlank( message = "Passwort ist erforderlich" )
   private String password;
}
