package com.flexjob.user.domain.exception;

/**
 * UserNotFoundException - Domain Exception für fehlende User
 *
 * Wird verwendet in:
 * - GetUserService wenn User-ID nicht existiert
 * - LoginUserService wenn E-Mail nicht gefunden wird
 * - UpdateUserService wenn zu aktualisierender User fehlt
 *
 * Zweck:
 * Explizite Exception für fehlende User im System.
 * Ermöglicht spezifische Fehlerbehandlung in oberen Schichten.
 *
 * Warum eigene Exception?
 * - Semantik: "User not found" ist klarer als NullPointerException
 * - Error-Handling: Controller kann spezifisch HTTP 404 zurückgeben
 * - Monitoring: Leichter zu tracken in Logs und Metrics
 * - Clean Code: Exceptions als Teil der Business-Logik
 *
 * Exception-Hierarchie:
 * RuntimeException (unchecked)
 *   └─ UserNotFoundException
 *
 * Warum RuntimeException?
 * - Moderne Java Best Practice: Keine Checked Exceptions für Business-Logik
 * - Vermeidet try-catch Boilerplate im gesamten Code
 * - Spring @ExceptionHandler kann RuntimeExceptions abfangen
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * User-ID des nicht gefundenen Users (für Logging)
     */
    private final String userId;

    /**
     * Konstruktor mit Message
     *
     * Wird verwendet in: Standard-Fällen mit einfacher Nachricht
     */
    public UserNotFoundException(String message) {
        super(message);
        this.userId = null;
    }

    /**
     * Konstruktor mit Message und User-ID
     *
     * Wird verwendet in: Wenn User-ID für Logging relevant ist
     *
     * @param message Fehlermeldung
     * @param userId User-ID die nicht gefunden wurde
     */
    public UserNotFoundException(String message, String userId) {
        super(message);
        this.userId = userId;
    }

    /**
     * Konstruktor mit Message und Cause
     *
     * Wird verwendet in: Wenn Exception durch andere Exception ausgelöst wurde
     * Beispiel: DB-Exception beim User-Laden
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.userId = null;
    }

    /**
     * Vollständiger Konstruktor
     *
     * Wird verwendet in: Komplexen Fehler-Szenarien
     */
    public UserNotFoundException(String message, String userId, Throwable cause) {
        super(message, cause);
        this.userId = userId;
    }

    /**
     * Gibt User-ID des nicht gefundenen Users zurück
     *
     * Wird verwendet in: Logging, Monitoring, Error-Responses
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Factory-Methode: User by ID not found
     *
     * Wird verwendet in: GetUserService
     * Warum Factory? Konsistente Fehlermeldungen
     */
    public static UserNotFoundException byId(String userId) {
        return new UserNotFoundException(
                "User mit ID nicht gefunden: " + userId,
                userId
        );
    }

    /**
     * Factory-Methode: User by Email not found
     *
     * Wird verwendet in: LoginUserService
     * Warum? Unterscheidung zwischen ID- und E-Mail-Suche
     */
    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException(
                "User mit E-Mail nicht gefunden: " + email
        );
    }

    /**
     * Factory-Methode: User by Username not found
     *
     * Wird verwendet in: Falls Username-Login implementiert wird
     */
    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException(
                "User mit Username nicht gefunden: " + username
        );
    }
}
