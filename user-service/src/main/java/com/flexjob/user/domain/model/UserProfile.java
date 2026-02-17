package com.flexjob.user.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * UserProfile Domain Entity - Erweiterte Benutzerinformationen
 *
 * Wird verwendet in:
 * - User Entity (One-to-One Beziehung)
 * - UpdateProfileService für Profil-Updates
 * - GetUserService für vollständige User-Daten
 *
 * Zweck:
 * Trennung von Authentifizierungs-Daten (User) und Profil-Daten (UserProfile).
 * Ermöglicht optionale Profil-Informationen ohne User-Entity zu überladen.
 *
 * Warum separate Entity?
 * - Single Responsibility: User = Auth, UserProfile = Persönliche Daten
 * - Performance: Profil kann lazy geladen werden
 * - Flexibilität: Verschiedene Profile für verschiedene UserTypes möglich
 */
@Getter
@Builder
public class UserProfile {

    /**
     * Vorname des Benutzers
     */
    private String firstName;

    /**
     * Nachname des Benutzers
     */
    private String lastName;

    /**
     * Telefonnummer (optional)
     */
    private String phoneNumber;

    /**
     * Geburtsdatum (für Altersverifizierung bei Jobbewerbungen)
     */
    private LocalDate dateOfBirth;

    /**
     * Adresse - kann für Bewerber wichtig sein
     */
    private String address;
    private String city;
    private String postalCode;
    private String country;

    /**
     * Profilbild URL (gespeichert in Cloud Storage)
     */
    private String profileImageUrl;

    /**
     * Bio/Über mich Text
     */
    private String bio;

    /**
     * LinkedIn Profil URL (für Professional Networking)
     */
    private String linkedInUrl;

    /**
     * GitHub Profil URL (für Tech-Jobs relevant)
     */
    private String githubUrl;

    /**
     * Website/Portfolio URL
     */
    private String websiteUrl;

    /**
     * Gibt den vollständigen Namen zurück
     *
     * Wird verwendet in: UI-Anzeige, E-Mail-Templates
     * Warum Methode? Konsistente Formatierung des Namens
     */
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return "";
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }

    /**
     * Prüft ob das Profil vollständig ausgefüllt ist
     *
     * Wird verwendet in: Profil-Vervollständigungs-Anzeige in UI
     * Warum wichtig? Vollständige Profile haben höhere Job-Match-Chancen
     */
    public boolean isComplete() {
        return firstName != null && !firstName.isEmpty()
                && lastName != null && !lastName.isEmpty()
                && phoneNumber != null && !phoneNumber.isEmpty()
                && address != null && !address.isEmpty();
    }

    /**
     * Berechnet Profil-Vervollständigung in Prozent
     *
     * Wird verwendet in: Dashboard für Profil-Progress-Bar
     * Warum? Gamification motiviert User zur vollständigen Profil-Eingabe
     */
    public int getCompletionPercentage() {
        int totalFields = 12; // Anzahl der relevanten Felder
        int filledFields = 0;

        if (firstName != null && !firstName.isEmpty()) filledFields++;
        if (lastName != null && !lastName.isEmpty()) filledFields++;
        if (phoneNumber != null && !phoneNumber.isEmpty()) filledFields++;
        if (dateOfBirth != null) filledFields++;
        if (address != null && !address.isEmpty()) filledFields++;
        if (city != null && !city.isEmpty()) filledFields++;
        if (postalCode != null && !postalCode.isEmpty()) filledFields++;
        if (country != null && !country.isEmpty()) filledFields++;
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) filledFields++;
        if (bio != null && !bio.isEmpty()) filledFields++;
        if (linkedInUrl != null && !linkedInUrl.isEmpty()) filledFields++;
        if (websiteUrl != null && !websiteUrl.isEmpty()) filledFields++;

        return (int) ((filledFields / (double) totalFields) * 100);
    }
}
