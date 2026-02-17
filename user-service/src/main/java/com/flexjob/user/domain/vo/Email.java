package com.flexjob.user.domain.vo;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email Value Object - Validierte E-Mail-Adresse
 *
 * Wird verwendet in:
 * - User Entity als email-Feld
 * - RegisterUserCommand zur Validierung
 * - LoginCommand zur Authentifizierung
 *
 * Zweck:
 * Kapselt E-Mail-Adresse mit eingebauter Validierung.
 * Garantiert dass alle Email-Objekte im System valide sind.
 *
 * Warum Value Object für Email?
 * 1. Validierung: E-Mail-Format wird EINMAL zentral geprüft
 * 2. Typsicherheit: Email != String
 * 3. Domain-Logik: Normalisierung (lowercase, trim)
 * 4. Geschäftsregeln: Nur bestimmte Domains erlauben (optional)
 *
 * Vorteile:
 * - Keine invaliden E-Mails im System möglich
 * - Validierung muss nicht bei jeder Verwendung wiederholt werden
 * - Einfache Erweiterung um Geschäftslogik (z.B. Blacklist-Check)
 */
public final class Email {

    /**
     * RFC 5322 vereinfachtes E-Mail-Pattern
     *
     * Warum nicht perfektes Pattern?
     * - Perfekte RFC-Validierung ist extrem komplex
     * - Praktische Validierung reicht für 99.9% der Fälle
     * - Finale Validierung erfolgt durch E-Mail-Versand
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final String value;

    /**
     * Privater Konstruktor mit Validierung
     *
     * Warum privat? Erzwingt Verwendung der of() Factory-Methode
     */
    private Email(String value) {
        // Schritt 1: Null-Check
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // Schritt 2: Normalisierung (immer lowercase, getrimmt)
        String normalized = value.trim().toLowerCase();

        // Schritt 3: Format-Validierung
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }

        // Schritt 4: Längen-Check (DB-Constraint)
        if (normalized.length() > 255) {
            throw new IllegalArgumentException("Email too long (max 255 characters)");
        }

        this.value = normalized;
    }

    /**
     * Erstellt Email Value Object aus String
     *
     * Wird verwendet in: Überall wo E-Mail-Eingabe erfolgt
     *
     * @param value E-Mail-String
     * @return Validiertes Email-Objekt
     * @throws IllegalArgumentException wenn E-Mail ungültig
     */
    public static Email of(String value) {
        return new Email(value);
    }

    /**
     * Gibt den E-Mail-Wert zurück
     *
     * Wird verwendet in: Persistierung, API-Responses, E-Mail-Versand
     */
    public String getValue() {
        return value;
    }

    /**
     * Extrahiert die Domain aus der E-Mail
     *
     * Wird verwendet in: Domain-basierte Logik (z.B. Corporate-Accounts)
     * Beispiel: "user@company.com" -> "company.com"
     */
    public String getDomain() {
        int atIndex = value.indexOf('@');
        return value.substring(atIndex + 1);
    }

    /**
     * Extrahiert den Local-Part (vor @)
     *
     * Wird verwendet in: Seltener, z.B. für Personalisierung
     * Beispiel: "user@company.com" -> "user"
     */
    public String getLocalPart() {
        int atIndex = value.indexOf('@');
        return value.substring(0, atIndex);
    }

    /**
     * Prüft ob E-Mail von bestimmter Domain ist
     *
     * Wird verwendet in: Corporate-Account-Validierung
     * Beispiel: isFromDomain("company.com") für Firmen-Registrierung
     */
    public boolean isFromDomain(String domain) {
        return getDomain().equalsIgnoreCase(domain);
    }

    /**
     * Maskiert E-Mail für sichere Anzeige
     *
     * Wird verwendet in: Logs, öffentliche API-Responses
     * Beispiel: "user@company.com" -> "u***@company.com"
     * Warum? DSGVO-Konformität, Datenschutz
     */
    public String getMasked() {
        String localPart = getLocalPart();
        if (localPart.length() <= 1) {
            return localPart + "***@" + getDomain();
        }
        return localPart.charAt(0) + "***@" + getDomain();
    }

    /**
     * Value Object Gleichheit über Wert
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    /**
     * HashCode für Collections
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * String-Repräsentation
     */
    @Override
    public String toString() {
        return "Email{" + getMasked() + "}"; // Maskiert für sichere Logs
    }
}
