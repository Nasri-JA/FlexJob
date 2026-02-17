package com.flexjob.user.domain.service;

import com.flexjob.user.domain.exception.UserNotFoundException;
import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;
import org.springframework.stereotype.Service;

/**
 * UserDomainService - Geschäftslogik-Service im Domain-Layer
 *
 * Wird verwendet in:
 * - Application Services (RegisterUserService, LoginUserService)
 * - Überall wo komplexe Domain-Logik über mehrere Entities hinweg nötig ist
 *
 * Zweck:
 * Kapselt Domain-Logik die nicht natürlich zu einer einzelnen Entity gehört.
 * Orchestriert Operationen über mehrere Domain-Objekte hinweg.
 *
 * Warum Domain Service?
 * - Manche Logik gehört nicht in eine einzelne Entity
 * - Beispiel: "Ist diese E-Mail bereits registriert?" betrifft mehrere User
 * - Domain Services haben keine technische Infrastruktur (kein DB-Zugriff)
 * - Sie arbeiten nur mit übergebenen Domain-Objekten
 *
 * Unterschied zu Application Service:
 * - Domain Service = Reine Geschäftslogik
 * - Application Service = Orchestrierung + Infrastruktur-Zugriff
 */
@Service
public class UserDomainService {

    /**
     * Validiert ob ein User registriert werden kann
     *
     * Wird verwendet in: RegisterUserService vor User-Erstellung
     *
     * Zweck:
     * Zentrale Geschäftsregeln für User-Registrierung:
     * - Passwort-Stärke
     * - E-Mail-Domain Whitelist/Blacklist
     * - Weitere fachliche Validierungen
     *
     * @param email E-Mail-Adresse
     * @param password Klartext-Passwort
     * @throws IllegalArgumentException wenn Validierung fehlschlägt
     */
    public void validateRegistration(Email email, String password) {
        // Validierung 1: Passwort-Stärke
        validatePasswordStrength(password);

        // Validierung 2: E-Mail-Domain (optional)
        validateEmailDomain(email);

        // Weitere fachliche Validierungen können hier hinzugefügt werden
    }

    /**
     * Validiert Passwort-Stärke
     *
     * Geschäftsregeln:
     * - Mindestens 8 Zeichen
     * - Mindestens 1 Großbuchstabe
     * - Mindestens 1 Kleinbuchstabe
     * - Mindestens 1 Zahl
     * - Mindestens 1 Sonderzeichen
     *
     * Warum so streng? Sicherheit gegen Brute-Force-Angriffe
     */
    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Passwort muss mindestens 8 Zeichen lang sein"
            );
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException(
                    "Passwort muss mindestens einen Großbuchstaben enthalten"
            );
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException(
                    "Passwort muss mindestens einen Kleinbuchstaben enthalten"
            );
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException(
                    "Passwort muss mindestens eine Zahl enthalten"
            );
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new IllegalArgumentException(
                    "Passwort muss mindestens ein Sonderzeichen enthalten"
            );
        }
    }

    /**
     * Validiert E-Mail-Domain
     *
     * Wird verwendet in: Registrierung zur Domain-Filterung
     *
     * Zweck:
     * - Blockiert Wegwerf-E-Mail-Dienste
     * - Erlaubt nur bestimmte Corporate-Domains (optional)
     *
     * Warum wichtig? Reduziert Spam-Registrierungen
     */
    private void validateEmailDomain(Email email) {
        String domain = email.getDomain();

        // Blacklist: Bekannte Wegwerf-E-Mail-Dienste
        String[] blockedDomains = {
                "tempmail.com",
                "throwaway.email",
                "guerrillamail.com",
                "10minutemail.com"
        };

        for (String blockedDomain : blockedDomains) {
            if (domain.equalsIgnoreCase(blockedDomain)) {
                throw new IllegalArgumentException(
                        "E-Mail-Domain ist nicht erlaubt: " + domain
                );
            }
        }

        // Optionale Whitelist für Corporate-Only Zugang
        // String[] allowedDomains = {"company.com", "flexjob.com"};
        // Implementierung bei Bedarf
    }

    /**
     * Prüft ob User aktiviert und verwendbar ist
     *
     * Wird verwendet in: LoginUserService, GetUserService
     *
     * Zweck:
     * Zentrale Validierung ob User-Account in gültigem Zustand ist
     *
     * @param user Zu prüfender User
     * @throws IllegalStateException wenn User nicht verwendbar
     */
    public void validateUserActive(User user) {
        if (!user.isActive()) {
            throw new IllegalStateException(
                    "User-Account ist deaktiviert: " + user.getId().getValue()
            );
        }
    }

    /**
     * Prüft ob User existiert und wirft Exception falls nicht
     *
     * Wird verwendet in: GetUserService, UpdateUserService
     *
     * Zweck:
     * Vermeidet NullPointerException durch frühe Validierung
     *
     * @param user User oder null
     * @param userId Gesuchte User-ID
     * @return User (nie null)
     * @throws UserNotFoundException wenn User nicht existiert
     */
    public User requireUserExists(User user, UserId userId) {
        if (user == null) {
            throw new UserNotFoundException(
                    "User nicht gefunden: " + userId.getValue()
            );
        }
        return user;
    }

    /**
     * Prüft ob User E-Mail verifiziert hat
     *
     * Wird verwendet in: Job-Application-Service, Premium-Features
     *
     * Zweck:
     * Bestimmte Funktionen erfordern verifizierte E-Mail
     *
     * @param user Zu prüfender User
     * @throws IllegalStateException wenn E-Mail nicht verifiziert
     */
    public void requireEmailVerified(User user) {
        if (!user.isEmailVerified()) {
            throw new IllegalStateException(
                    "E-Mail muss verifiziert sein. Bitte prüfen Sie Ihren Posteingang."
            );
        }
    }

    /**
     * Vergleicht zwei User auf Gleichheit
     *
     * Wird verwendet in: Testing, Admin-Tools
     *
     * Zweck:
     * Business-Gleichheit (nicht technische Object-Gleichheit)
     */
    public boolean isSameUser(User user1, User user2) {
        if (user1 == null || user2 == null) {
            return false;
        }
        return user1.getId().equals(user2.getId());
    }
}
