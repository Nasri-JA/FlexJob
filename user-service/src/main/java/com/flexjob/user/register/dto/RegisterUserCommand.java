package com.flexjob.user.register.dto;

import com.flexjob.user.domain.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserCommand
{
   @NotBlank( message = "E-Mail ist erforderlich" )
   @Email( message = "Ungültiges E-Mail-Format" )
   @Size( max = 255, message = "E-Mail darf maximal 255 Zeichen haben" )
   private String email;

   @NotBlank( message = "Passwort ist erforderlich" )
   @Size( min = 8, message = "Passwort muss mindestens 8 Zeichen haben" )
   private String password;

   @NotNull( message = "Benutzertyp ist erforderlich" )
   private UserType userType;

   @Size( max = 100, message = "Vorname darf maximal 100 Zeichen haben" )
   private String firstName;

   @Size( max = 100, message = "Nachname darf maximal 100 Zeichen haben" )
   private String lastName;

}
