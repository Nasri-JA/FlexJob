package com.flexjob.user.application.service;

import com.flexjob.user.application.dto.command.RegisterUserCommand;
import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.application.port.in.RegisterUserUseCase;
import com.flexjob.user.application.port.out.LoadUserPort;
import com.flexjob.user.application.port.out.PublishEventPort;
import com.flexjob.user.application.port.out.SaveUserPort;
import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.model.UserProfile;
import com.flexjob.user.domain.service.UserDomainService;
import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * RegisterUserService - Application Service für User-Registrierung
 *
 * Wird verwendet in:
 * - AuthController (über RegisterUserUseCase Interface)
 *
 * Zweck:
 * Implementiert den kompletten User-Registrierungs-Workflow.
 * Orchestriert Domain-Logik, Persistierung und Event-Publishing.
 *
 * Warum Application Service?
 * - Orchestrierung: Koordiniert mehrere Ports und Domain-Services
 * - Use Case Implementierung: 1 Service = 1 Use Case
 * - Transaktions-Grenze: Definiert ACID-Transaktionen
 * - Technische Concerns: Passwort-Hashing, Event-Publishing
 *
 * Unterschied zu Domain Service:
 * - Domain Service = Reine Business-Logik (keine Infrastruktur)
 * - Application Service = Orchestrierung + Infrastruktur-Zugriff (Ports)
 *
 * Clean Architecture Layers:
 * Controller (Infrastructure) -> RegisterUserUseCase (Application Port)
 *   -> RegisterUserService (Application) -> Domain Services + Output Ports
 *     -> Domain Model + Persistence Adapter (Infrastructure)
 */
@Service
@RequiredArgsConstructor
@Slf4j // Lombok: Erstellt Logger-Instanz
@Transactional // Spring: Gesamte Methode in Transaktion (Rollback bei Exception)
public class RegisterUserService implements RegisterUserUseCase {

    /**
     * Dependencies werden per Constructor Injection injected
     * Warum Constructor Injection?
     * - Immutability (final Felder)
     * - Testbarkeit (Mocking einfacher)
     * - Null-Safety (Spring wirft Fehler wenn Dependency fehlt)
     * - Best Practice seit Spring 4.3
     */

    private final LoadUserPort loadUserPort;        // User-Laden aus DB
    private final SaveUserPort saveUserPort;        // User-Speichern in DB
    private final PublishEventPort publishEventPort; // Event-Publishing
    private final UserDomainService domainService;   // Domain-Validierung
    private final PasswordEncoder passwordEncoder;   // BCrypt Password-Hashing

    /**
     * Registriert neuen User im System
     *
     * SCHRITT-FÜR-SCHRITT ABLAUF:
     */
    @Override
    public UserResponse register(RegisterUserCommand command) {
        log.info("Registering new user with email: {}", command.getEmail());

        // ==========================================
        // SCHRITT 1: Command-Validierung
        // ==========================================
        // Prüft ob Command-Objekt valide ist (nicht null, etc.)
        validateCommand(command);

        // ==========================================
        // SCHRITT 2: Email Value Object erstellen
        // ==========================================
        // Email.of() validiert Format und normalisiert (lowercase, trim)
        // Wirft IllegalArgumentException bei ungültigem Format
        Email email = Email.of(command.getEmail());
        log.debug("Email validated and normalized: {}", email.getValue());

        // ==========================================
        // SCHRITT 3: Duplikat-Check
        // ==========================================
        // Prüft ob E-Mail bereits registriert ist
        // Geschäftsregel: Eine E-Mail = Ein Account
        if (loadUserPort.existsByEmail(email)) {
            log.warn("Registration attempt with existing email: {}", email.getMasked());
            throw new IllegalArgumentException(
                    "E-Mail bereits registriert: " + email.getValue()
            );
        }

        // ==========================================
        // SCHRITT 4: Domain-Validierung
        // ==========================================
        // UserDomainService prüft Business-Regeln:
        // - Passwort-Stärke (min 8 Zeichen, Groß/Klein, Zahl, Sonderzeichen)
        // - E-Mail-Domain (keine Wegwerf-E-Mails)
        domainService.validateRegistration(email, command.getPassword());
        log.debug("Domain validation passed");

        // ==========================================
        // SCHRITT 5: Passwort verschlüsseln
        // ==========================================
        // BCrypt-Hashing (langsam = sicher gegen Brute-Force)
        // NIEMALS Klartext-Passwort speichern!
        // BCrypt generiert automatisch Salt und speichert ihn im Hash
        String passwordHash = passwordEncoder.encode(command.getPassword());
        log.debug("Password hashed with BCrypt");

        // ==========================================
        // SCHRITT 6: User Domain-Model erstellen
        // ==========================================
        // Builder-Pattern für saubere Objekt-Erstellung
        User user = User.builder()
                .id(UserId.generate())                    // Neue UUID generieren
                .email(email)                             // Validierte E-Mail
                .passwordHash(passwordHash)               // Verschlüsseltes Passwort
                .userType(command.getUserType())          // CANDIDATE, EMPLOYER, ADMIN
                .active(true)                             // Account ist aktiv
                .emailVerified(false)                     // Noch nicht verifiziert
                .createdAt(LocalDateTime.now())           // Aktueller Zeitstempel
                .updatedAt(LocalDateTime.now())
                .profile(createDefaultProfile(command))   // Leeres Profil erstellen
                .build();

        log.debug("User domain model created with ID: {}", user.getId().getValue());

        // ==========================================
        // SCHRITT 7: User persistieren
        // ==========================================
        // SaveUserPort mapped Domain-Model zu JPA-Entity und speichert in DB
        // Gibt User mit generierten DB-Feldern zurück (ID, Timestamps)
        User savedUser = saveUserPort.save(user);
        log.info("User successfully saved to database: {}", savedUser.getId().getValue());

        // ==========================================
        // SCHRITT 8: Event publishen
        // ==========================================
        // Andere Services (Email-Service) können auf Event reagieren
        // Asynchrone Verarbeitung: Verifizierungs-E-Mail versenden
        publishUserRegisteredEvent(savedUser);
        log.debug("UserRegisteredEvent published");

        // ==========================================
        // SCHRITT 9: Response-DTO erstellen
        // ==========================================
        // Domain-Model zu UserResponse mappen (OHNE Passwort!)
        // Response für REST-API oder andere Clients
        UserResponse response = mapToUserResponse(savedUser);
        log.info("User registration completed successfully for: {}", email.getMasked());

        return response;
    }

    /**
     * Validiert das RegisterUserCommand
     *
     * Prüft auf null-Werte und Pflichtfelder
     */
    private void validateCommand(RegisterUserCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("RegisterUserCommand cannot be null");
        }
        if (command.getEmail() == null || command.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (command.getPassword() == null || command.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (command.getUserType() == null) {
            throw new IllegalArgumentException("UserType is required");
        }
    }

    /**
     * Erstellt leeres Standard-Profil für neuen User
     *
     * Wird verwendet in: User-Erstellung
     * Warum? Vermeidet null-Profile, User hat immer Profil-Objekt
     */
    private UserProfile createDefaultProfile(RegisterUserCommand command) {
        return UserProfile.builder()
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                // Weitere Felder werden später vom User ausgefüllt
                .build();
    }

    /**
     * Published UserRegisteredEvent für andere Services
     *
     * Event-Struktur:
     * - userId: Für Identifikation
     * - email: Für E-Mail-Versand
     * - userType: Für typspezifische Logik
     */
    private void publishUserRegisteredEvent(User user) {
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId().getValue())
                .email(user.getEmail().getValue())
                .userType(user.getUserType())
                .timestamp(LocalDateTime.now())
                .build();

        publishEventPort.publish(event, "user.registered");
    }

    /**
     * Mapped User Domain-Model zu UserResponse DTO
     *
     * Wichtig: Passwort-Hash wird NICHT gemappt!
     * Nur öffentlich sichtbare Daten
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().getValue())
                .email(user.getEmail().getValue())
                .userType(user.getUserType())
                .active(user.isActive())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .profile(user.getProfile())
                .build();
    }

    /**
     * Inner Class: UserRegisteredEvent
     *
     * In Production: Event-Klassen ins common-Module auslagern
     * Warum? Andere Services benötigen Event-Definition
     */
    @lombok.Builder
    @lombok.Getter
    private static class UserRegisteredEvent {
        private String userId;
        private String email;
        private com.flexjob.user.domain.model.UserType userType;
        private LocalDateTime timestamp;
    }
}
