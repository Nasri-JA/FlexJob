package com.flexjob.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * LoginResponse - Response-DTO für erfolgreichen Login
 *
 * Wird verwendet in:
 * - LoginUserService (nach erfolgreicher Authentifizierung)
 * - AuthController (API-Response für /auth/login)
 *
 * Zweck:
 * Gibt JWT-Tokens und User-Daten nach erfolgreichem Login zurück.
 *
 * Warum eigenes DTO statt nur UserResponse?
 * - Enthält zusätzlich JWT-Tokens
 * - Tokens gehören nicht zum User-Objekt
 * - Spezifisch für Login-Flow
 */
@Getter
@Builder
public class LoginResponse {

    /**
     * JWT Access Token
     *
     * Verwendung:
     * - Wird bei jedem API-Request mitgeschickt (Authorization Header)
     * - Kurze Lebensdauer (z.B. 15 Minuten)
     * - Enthält: userId, email, roles
     *
     * Format: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     */
    private String accessToken;

    /**
     * JWT Refresh Token
     *
     * Verwendung:
     * - Zum Erneuern des Access Tokens
     * - Lange Lebensdauer (z.B. 7 Tage)
     * - Wird nur beim /auth/refresh Endpoint verwendet
     *
     * Security:
     * - Sollte in HttpOnly Cookie gespeichert werden
     * - Oder in Secure Storage (nicht localStorage!)
     * - Kann in DB gespeichert und widerrufen werden
     */
    private String refreshToken;

    /**
     * Token-Typ (immer "Bearer")
     *
     * Wird für Authorization Header benötigt:
     * Authorization: Bearer <accessToken>
     */
    private String tokenType = "Bearer";

    /**
     * Access Token Ablaufzeit in Sekunden
     *
     * Client kann Timer starten um Token zu erneuern
     * Beispiel: 900 = 15 Minuten
     */
    private Long expiresIn;

    /**
     * User-Daten des eingeloggten Users
     *
     * Frontend kann damit UI personalisieren:
     * - Name anzeigen
     * - UserType-spezifische Menüs
     * - Profilbild
     */
    private UserResponse user;
}
