package com.flexjob.user.application.port.in;

import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.domain.vo.UserId;

/**
 * GetUserUseCase - Input Port für User-Abfrage
 *
 * Wird verwendet in:
 * - UserController (REST API Endpoint /users/{id})
 * - GetUserService als Implementierung
 * - Andere Services die User-Daten benötigen
 *
 * Zweck:
 * Definiert den Vertrag für das Abrufen von User-Informationen.
 *
 * Use Case: "Als System möchte ich User-Daten abrufen können"
 */
public interface GetUserUseCase {

    /**
     * Gibt User-Daten anhand der User-ID zurück
     *
     * Ablauf:
     * 1. System empfängt User-ID
     * 2. System lädt User aus Datenbank
     * 3. System prüft ob User existiert
     * 4. System mapped Domain-Model zu Response-DTO
     * 5. System gibt User-Daten zurück (ohne Passwort!)
     *
     * @param userId Eindeutige User-ID
     * @return UserResponse mit User-Daten (ohne sensible Informationen)
     *
     * @throws com.flexjob.user.domain.exception.UserNotFoundException wenn User nicht existiert
     *
     * Sicherheits-Hinweise:
     * - Passwort-Hash wird NICHT zurückgegeben
     * - Sensible Daten können gefiltert werden
     * - Authorization-Check sollte im Controller erfolgen
     */
    UserResponse getUserById(UserId userId);

    /**
     * Gibt den aktuell eingeloggten User zurück
     *
     * Wird verwendet in: "/users/me" Endpoint
     *
     * Ablauf:
     * 1. System extrahiert User-ID aus JWT-Token (Security Context)
     * 2. System lädt User-Daten
     * 3. System gibt User-Daten zurück
     *
     * @return UserResponse mit Daten des eingeloggten Users
     *
     * Warum separate Methode?
     * - Häufiger Use Case: "Zeige meine Profil-Daten"
     * - Keine User-ID nötig (aus Token)
     * - Unterschiedliche Authorization-Logik
     */
    UserResponse getCurrentUser();
}
