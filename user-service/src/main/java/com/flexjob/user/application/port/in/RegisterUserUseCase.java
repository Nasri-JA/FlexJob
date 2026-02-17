package com.flexjob.user.application.port.in;

import com.flexjob.user.application.dto.command.RegisterUserCommand;
import com.flexjob.user.application.dto.response.UserResponse;

/**
 * RegisterUserUseCase - Input Port für User-Registrierung
 *
 * Wird verwendet in:
 * - AuthController (REST API Endpoint /auth/register)
 * - RegisterUserService als Implementierung
 *
 * Zweck:
 * Definiert den Vertrag (Contract) für User-Registrierung.
 * Teil des Hexagonal Architecture (Ports & Adapters) Patterns.
 *
 * Was ist ein Port?
 * - Interface das definiert WAS die Anwendung kann
 * - Unabhängig von WIE es implementiert wird
 * - Input Ports = Was die Anwendung anbietet (Use Cases)
 * - Output Ports = Was die Anwendung benötigt (z.B. DB)
 *
 * Warum Ports Pattern?
 * 1. Abhängigkeiten zeigen nach INNEN (Dependency Inversion)
 *    - Controller kennt Port (Interface)
 *    - Service implementiert Port
 *    - Domain ist unabhängig von Infrastruktur
 *
 * 2. Austauschbare Adapter
 *    - REST Controller verwendet Port
 *    - GraphQL Resolver könnte gleichen Port verwenden
 *    - CLI Tool könnte gleichen Port verwenden
 *
 * 3. Testbarkeit
 *    - Port kann gemockt werden
 *    - Keine Abhängigkeit zu konkreter Implementierung
 *
 * 4. Klare Architektur-Grenzen
 *    - Application Core ist geschützt
 *    - Externe Welt kommuniziert nur über Ports
 *
 * Use Case = Geschäftlicher Anwendungsfall
 * "Als neuer User möchte ich mich registrieren können"
 */
public interface RegisterUserUseCase {

    /**
     * Registriert einen neuen User im System
     *
     * Ablauf (aus Business-Sicht):
     * 1. User gibt E-Mail, Passwort und UserType ein
     * 2. System validiert die Eingaben
     * 3. System prüft ob E-Mail bereits existiert
     * 4. System erstellt User-Account mit verschlüsseltem Passwort
     * 5. System versendet Verifizierungs-E-Mail
     * 6. System gibt User-Daten zurück (ohne Passwort!)
     *
     * @param command Registrierungs-Daten (E-Mail, Passwort, UserType)
     * @return UserResponse mit erstelltem User (ohne sensible Daten)
     *
     * @throws IllegalArgumentException wenn Validierung fehlschlägt
     * @throws com.flexjob.user.domain.exception.EmailAlreadyExistsException wenn E-Mail bereits registriert
     */
    UserResponse register(RegisterUserCommand command);

    /**
     * Hinweis zur Implementierung:
     *
     * Die konkrete Implementierung (RegisterUserService) wird:
     * - Command validieren
     * - Domain-Service für Business-Validierung nutzen
     * - Passwort verschlüsseln (BCrypt)
     * - User über SaveUserPort persistieren
     * - Event über PublishEventPort publishen
     * - UserResponse zurückgeben
     *
     * Controller (Adapter) wird:
     * - HTTP Request empfangen
     * - RegisterRequest DTO zu RegisterUserCommand mappen
     * - Use Case aufrufen
     * - UserResponse zu HTTP Response mappen
     * - Bei Exception: Passenden HTTP Status zurückgeben
     */
}
