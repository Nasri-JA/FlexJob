package com.flexjob.user.application.dto.response;

import com.flexjob.user.domain.model.UserProfile;
import com.flexjob.user.domain.model.UserType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * UserResponse - Response-DTO für User-Daten
 *
 * Wird verwendet in:
 * - RegisterUserService (nach erfolgreicher Registrierung)
 * - GetUserService (beim Abrufen von User-Daten)
 * - UpdateUserService (nach Update)
 * - AuthController, UserController (API-Responses)
 *
 * Zweck:
 * Kapselt User-Daten für Ausgabe an Clients (REST API, Frontend).
 * Enthält NUR öffentlich sichtbare Daten.
 *
 * Warum Response-DTO statt Domain-Model?
 * 1. Security
 *    - Passwort-Hash wird NICHT exposed
 *    - Sensible Daten können gefiltert werden
 *    - Kontrolle über was nach außen geht
 *
 * 2. API-Stabilität
 *    - Domain-Model kann sich ändern
 *    - API-Contract bleibt stabil
 *    - Versionierung möglich
 *
 * 3. Optimierung
 *    - Nur benötigte Felder
 *    - Flache Struktur (keine tiefen Navigationen)
 *    - JSON-Serialisierung optimiert
 *
 * 4. Mehrere Representations
 *    - UserSummaryResponse (weniger Felder)
 *    - UserDetailResponse (mehr Felder)
 *    - AdminUserResponse (mit Admin-Infos)
 */
@Getter
@Builder
public class UserResponse {

    /**
     * User-ID als String (UUID)
     */
    private String id;

    /**
     * E-Mail-Adresse
     */
    private String email;

    /**
     * User-Typ (CANDIDATE, EMPLOYER, ADMIN)
     */
    private UserType userType;

    /**
     * Account-Status
     */
    private boolean active;

    /**
     * E-Mail-Verifizierungs-Status
     */
    private boolean emailVerified;

    /**
     * Zeitstempel der Erstellung
     */
    private LocalDateTime createdAt;

    /**
     * Zeitstempel der letzten Änderung
     */
    private LocalDateTime updatedAt;

    /**
     * User-Profil mit persönlichen Daten
     *
     * Kann null sein wenn Profil noch nicht ausgefüllt
     */
    private UserProfile profile;

    /**
     * Hinweis: Passwort-Hash ist NICHT enthalten!
     * Wird niemals nach außen gegeben aus Sicherheitsgründen
     */
}
