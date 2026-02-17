package com.flexjob.user.application.dto.command;

import lombok.Builder;
import lombok.Getter;

/**
 * LoginCommand - Command-DTO für User-Login
 *
 * Wird verwendet in:
 * - AuthController (erstellt aus LoginRequest)
 * - LoginUserService (verarbeitet Command)
 *
 * Zweck:
 * Kapselt Login-Credentials für Authentifizierung.
 *
 * Warum separates Command?
 * - Klare Trennung: Login != User-Objekt
 * - Security: Nur benötigte Felder (email + password)
 * - Validierung: Spezifisch für Login-Flow
 */
@Getter
@Builder
public class LoginCommand {

    /**
     * E-Mail-Adresse für Login
     */
    private final String email;

    /**
     * Passwort im Klartext
     *
     * WICHTIG:
     * - Wird mit gespeichertem Hash verglichen (BCrypt)
     * - NIE loggen oder persistent speichern!
     * - Nach Vergleich aus Speicher löschen
     */
    private final String password;
}
