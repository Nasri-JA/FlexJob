package com.flexjob.user.domain.model;

/**
 * UserType Enumeration - Benutzer-Rollen im System
 *
 * Wird verwendet in:
 * - User Entity zur Typ-Bestimmung
 * - SecurityConfig für rollenbasierte Zugriffskontrolle
 * - Job-Service zur Berechtigung (nur EMPLOYER kann Jobs posten)
 *
 * Zweck:
 * Definiert die verschiedenen Benutzertypen in der FlexJob-Plattform.
 * Jeder Typ hat unterschiedliche Berechtigungen und Funktionen.
 *
 * Warum Enum statt String?
 * - Typsicherheit zur Compile-Zeit
 * - Keine Tippfehler möglich
 * - IDE-Unterstützung mit Autocomplete
 * - Einfache Erweiterung um Methoden
 */
public enum UserType {

    /**
     * CANDIDATE - Jobsuchender
     *
     * Berechtigungen:
     * - Jobs suchen und filtern
     * - Auf Jobs bewerben
     * - Bewerbungen verwalten
     * - Profil mit CV erstellen
     * - Job-Alerts abonnieren
     */
    CANDIDATE("Kandidat"),

    /**
     * EMPLOYER - Arbeitgeber/Firma
     *
     * Berechtigungen:
     * - Jobs erstellen und veröffentlichen
     * - Bewerbungen einsehen
     * - Kandidaten kontaktieren
     * - Firma-Profil verwalten
     * - Statistiken einsehen
     */
    EMPLOYER("Arbeitgeber"),

    /**
     * ADMIN - System-Administrator
     *
     * Berechtigungen:
     * - Alle User verwalten
     * - Jobs moderieren
     * - System-Einstellungen ändern
     * - Audit-Logs einsehen
     * - Support-Anfragen bearbeiten
     */
    ADMIN("Administrator");

    /**
     * Deutsche Bezeichnung für UI-Anzeige
     */
    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Prüft ob der User ein Kandidat ist
     *
     * Wird verwendet in: Job-Application-Service
     * Warum? Nur Kandidaten können sich bewerben
     */
    public boolean isCandidate() {
        return this == CANDIDATE;
    }

    /**
     * Prüft ob der User ein Arbeitgeber ist
     *
     * Wird verwendet in: Job-Creation-Service
     * Warum? Nur Arbeitgeber können Jobs posten
     */
    public boolean isEmployer() {
        return this == EMPLOYER;
    }

    /**
     * Prüft ob der User ein Admin ist
     *
     * Wird verwendet in: SecurityConfig für Admin-Endpoints
     * Warum? Admin-Funktionen erfordern höchste Berechtigung
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Gibt die Spring Security Role zurück
     *
     * Wird verwendet in: JWT Token Generation, SecurityConfig
     * Warum ROLE_ Prefix? Spring Security Convention
     */
    public String getRole() {
        return "ROLE_" + this.name();
    }
}
