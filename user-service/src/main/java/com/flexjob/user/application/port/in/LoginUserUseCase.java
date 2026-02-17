package com.flexjob.user.application.port.in;

import com.flexjob.user.application.dto.command.LoginCommand;
import com.flexjob.user.application.dto.response.LoginResponse;

/**
 * LoginUserUseCase - Input Port für User-Login
 *
 * Wird verwendet in:
 * - AuthController (REST API Endpoint /auth/login)
 * - LoginUserService als Implementierung
 *
 * Zweck:
 * Definiert den Vertrag für User-Authentifizierung.
 * Teil des Hexagonal Architecture Patterns.
 *
 * Use Case: "Als registrierter User möchte ich mich einloggen können"
 *
 * Authentifizierung vs Autorisierung:
 * - Authentifizierung (Authentication) = "Wer bist du?" -> Dieser Use Case
 * - Autorisierung (Authorization) = "Was darfst du?" -> Spring Security
 */
public interface LoginUserUseCase {

    /**
     * Authentifiziert einen User und gibt JWT-Token zurück
     *
     * Ablauf (aus Business-Sicht):
     * 1. User gibt E-Mail und Passwort ein
     * 2. System sucht User anhand E-Mail
     * 3. System vergleicht Passwort-Hash
     * 4. System prüft ob User aktiv ist
     * 5. System generiert JWT Access Token
     * 6. System generiert JWT Refresh Token
     * 7. System gibt Tokens und User-Daten zurück
     *
     * @param command Login-Daten (E-Mail, Passwort)
     * @return LoginResponse mit JWT-Tokens und User-Daten
     *
     * @throws com.flexjob.user.domain.exception.UserNotFoundException wenn E-Mail nicht existiert
     * @throws com.flexjob.user.application.exception.InvalidPasswordException wenn Passwort falsch
     * @throws IllegalStateException wenn User deaktiviert ist
     *
     * Security-Hinweise:
     * - Passwort wird NIE im Klartext gespeichert
     * - Passwort-Vergleich über BCrypt (langsam = sicher)
     * - Bei falschen Credentials: Gleiche Fehlermeldung (verhindert User-Enumeration)
     * - Rate-Limiting sollte in Gateway/Controller erfolgen
     */
    LoginResponse login(LoginCommand command);

    /**
     * JWT Token Struktur (für Verständnis):
     *
     * Access Token (kurze Lebensdauer, z.B. 15 Min):
     * - Enthält: userId, email, userType, roles
     * - Wird bei jedem API-Request mitgeschickt
     * - Läuft schnell ab (Sicherheit)
     *
     * Refresh Token (lange Lebensdauer, z.B. 7 Tage):
     * - Enthält: userId
     * - Wird nur zum Erneuern des Access Tokens verwendet
     * - Sollte sicher gespeichert werden (HttpOnly Cookie oder Secure Storage)
     *
     * Warum zwei Tokens?
     * - Access Token: Schnell, oft verwendet, kurze Lebensdauer
     * - Refresh Token: Selten verwendet, lange Lebensdauer, kann widerrufen werden
     * - Kompromittierter Access Token ist nur kurz gültig
     * - Refresh Token kann in DB gespeichert und invalidiert werden
     */
}
