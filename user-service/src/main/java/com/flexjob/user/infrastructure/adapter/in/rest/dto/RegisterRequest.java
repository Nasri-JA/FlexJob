package com.flexjob.user.infrastructure.adapter.in.rest.dto;

import com.flexjob.user.domain.model.UserType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * RegisterRequest - REST-DTO für Registrierungs-Request
 *
 * Wird verwendet in:
 * - AuthController als @RequestBody
 * - Wird zu RegisterUserCommand gemappt
 *
 * Zweck:
 * Empfängt und validiert User-Eingaben von HTTP-Request.
 *
 * Warum separate DTO-Klasse?
 * - API-Layer (REST) != Application-Layer (Command)
 * - JSR-303 Validierung (@NotNull, @Email, etc.)
 * - API-Versionierung (RegisterRequestV2 möglich)
 * - Jackson Serialisierung (@JsonProperty für Naming)
 *
 * Validierung:
 * - Technisch: Format, required, Länge
 * - Business-Validierung erfolgt in Service
 */
@Data
public class RegisterRequest {

    /**
     * E-Mail-Adresse
     *
     * Validierung:
     * - @NotBlank: Darf nicht null oder leer sein
     * - @Email: Muss gültiges E-Mail-Format haben
     * - @Size: Max. 255 Zeichen (DB-Constraint)
     */
    @NotBlank(message = "E-Mail ist erforderlich")
    @Email(message = "Ungültiges E-Mail-Format")
    @Size(max = 255, message = "E-Mail darf maximal 255 Zeichen haben")
    private String email;

    /**
     * Passwort
     *
     * Validierung:
     * - @NotBlank: Darf nicht leer sein
     * - @Size: Min. 8 Zeichen (Business-Rule)
     * - Weitere Validierung (Komplexität) in UserDomainService
     */
    @NotBlank(message = "Passwort ist erforderlich")
    @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen haben")
    private String password;

    /**
     * User-Typ (CANDIDATE, EMPLOYER, ADMIN)
     *
     * Validierung:
     * - @NotNull: Muss angegeben werden
     *
     * JSON: "userType": "CANDIDATE"
     */
    @NotNull(message = "Benutzertyp ist erforderlich")
    private UserType userType;

    /**
     * Optionaler Vorname
     */
    @Size(max = 100, message = "Vorname darf maximal 100 Zeichen haben")
    private String firstName;

    /**
     * Optionaler Nachname
     */
    @Size(max = 100, message = "Nachname darf maximal 100 Zeichen haben")
    private String lastName;
}
