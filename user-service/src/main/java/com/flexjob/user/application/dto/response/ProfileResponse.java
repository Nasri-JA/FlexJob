package com.flexjob.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * ProfileResponse - Response-DTO für User-Profil
 *
 * Wird verwendet in:
 * - GetUserService (beim Abrufen von Profil-Daten)
 * - UpdateProfileService (nach Profil-Update)
 * - UserController (API-Response für /users/{id}/profile)
 *
 * Zweck:
 * Gibt detaillierte Profil-Informationen zurück.
 * Kann separat von UserResponse verwendet werden.
 *
 * Warum separate Response?
 * - Profil-Daten können optional geladen werden (Performance)
 * - Unterschiedliche Sichtbarkeit (öffentlich vs privat)
 * - Verschiedene Endpoints (/profile, /profile/public)
 */
@Getter
@Builder
public class ProfileResponse {

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

    /**
     * Profil-Vervollständigung in Prozent
     * Motiviert User zum Ausfüllen
     */
    private Integer completionPercentage;
}
