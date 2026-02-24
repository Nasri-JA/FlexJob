package com.flexjob.user.infrastructure.adapter.input.rest.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest
{
   @NotBlank( message = "E-Mail ist erforderlich" )
   @Email( message = "Ungültiges E-Mail-Format" )
   private String email;

   @NotBlank( message = "Passwort ist erforderlich" )
   private String password;
}
