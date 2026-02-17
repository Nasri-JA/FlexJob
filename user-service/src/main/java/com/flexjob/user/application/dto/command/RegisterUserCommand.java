package com.flexjob.user.application.dto.command;

import com.flexjob.user.domain.model.UserType;
import lombok.Builder;
import lombok.Getter;

/**
 * RegisterUserCommand - Command-DTO für User-Registrierung
 *
 * Wird verwendet in:
 * - AuthController (erstellt aus RegisterRequest)
 * - RegisterUserService (verarbeitet Command)
 *
 * Zweck:
 * Kapselt alle Daten die für User-Registrierung benötigt werden.
 * Input für den RegisterUserUseCase.
 *
 * Was ist ein Command?
 * - DTO (Data Transfer Object) für Befehle an die Anwendung
 * - Teil des CQRS-Patterns (Command Query Responsibility Segregation)
 * - Command = Schreib-Operation (Create, Update, Delete)
 * - Query = Lese-Operation (Get, List, Search)
 *
 * Warum separates Command-DTO statt direkt Domain-Model?
 * 1. Entkopplung
 *    - REST-API DTO (RegisterRequest) != Application DTO (Command) != Domain Model (User)
 *    - Jede Schicht hat eigene DTOs
 *    - API kann sich ändern ohne Domain zu ändern
 *
 * 2. Validierung auf verschiedenen Ebenen
 *    - REST-DTO: Technische Validierung (Format, required)
 *    - Command: Business-Validierung (Passwort-Stärke)
 *    - Domain: Geschäftsregeln (E-Mail-Duplikat)
 *
 * 3. Explizite Intention
 *    - Command zeigt klar: "Ich möchte registrieren"
 *    - Nicht: "Hier ist ein User-Objekt, mach was damit"
 *
 * 4. Security
 *    - Command enthält nur Input-Daten
 *    - Keine System-generierten Felder (ID, Timestamps)
 *    - Verhindert Mass-Assignment Vulnerabilities
 *
 * Command vs Domain Model:
 * - Command: Klartext-Passwort (vom User eingegeben)
 * - Domain Model: Passwort-Hash (verschlüsselt)
 */
@Getter
@Builder
public class RegisterUserCommand {

    /**
     * E-Mail-Adresse des neuen Users
     *
     * Wird zu Email Value Object konvertiert in Service
     */
    private final String email;

    /**
     * Passwort im Klartext
     *
     * WICHTIG:
     * - Wird im Service mit BCrypt gehasht
     * - NIE im Klartext speichern!
     * - Command-Objekt sollte nach Verarbeitung verworfen werden
     * - Nicht loggen! (Sicherheitsrisiko)
     */
    private final String password;

    /**
     * Benutzertyp (CANDIDATE, EMPLOYER, ADMIN)
     *
     * Bestimmt welche Funktionen dem User zur Verfügung stehen
     */
    private final UserType userType;

    /**
     * Optionaler Vorname
     *
     * Kann bei Registrierung angegeben werden
     * Oder später im Profil ergänzt werden
     */
    private final String firstName;

    /**
     * Optionaler Nachname
     *
     * Kann bei Registrierung angegeben werden
     * Oder später im Profil ergänzt werden
     */
    private final String lastName;

    /**
     * Builder-Pattern (Lombok @Builder)
     *
     * Verwendung:
     * RegisterUserCommand command = RegisterUserCommand.builder()
     *     .email("user@example.com")
     *     .password("SecurePass123!")
     *     .userType(UserType.CANDIDATE)
     *     .firstName("Max")
     *     .lastName("Mustermann")
     *     .build();
     *
     * Vorteile:
     * - Lesbar: .email() statt Konstruktor mit vielen Parametern
     * - Flexibel: Optional Felder können weggelassen werden
     * - Immutable: Felder sind final
     */
}
