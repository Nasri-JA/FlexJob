package com.flexjob.user.infrastructure.adapter.in.rest.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * LoginRequest - REST-DTO für Login-Request
 *
 * Wird verwendet in:
 * - AuthController als @RequestBody
 * - Wird zu LoginCommand gemappt
 *
 * Zweck:
 * Empfängt Login-Credentials von HTTP-Request.
 */
@Data
public class LoginRequest {

    @NotBlank(message = "E-Mail ist erforderlich")
    @Email(message = "Ungültiges E-Mail-Format")
    private String email;

    @NotBlank(message = "Passwort ist erforderlich")
    private String password;
}
