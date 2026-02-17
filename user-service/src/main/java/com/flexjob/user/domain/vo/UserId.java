package com.flexjob.user.domain.vo;

import java.util.Objects;
import java.util.UUID;

/**
 * UserId Value Object - Typsichere User-ID
 *
 * Wird verwendet in:
 * - User Entity als ID-Feld
 * - Alle Use Cases zur User-Identifikation
 * - DTOs und Commands
 *
 * Zweck:
 * Kapselt die User-ID als Value Object statt primitiven String/Long.
 * Verhindert versehentliche Verwechslung mit anderen IDs (JobId, CompanyId, etc.).
 *
 * Warum Value Object Pattern?
 * 1. Typsicherheit: Compiler verhindert falsche ID-Typen
 *    Beispiel: getUserById(jobId) würde nicht kompilieren
 * 2. Validierung: ID-Format wird zentral geprüft
 * 3. Immutabilität: Value Objects sind unveränderlich
 * 4. Semantik: Code wird selbstdokumentierend
 *
 * Was ist ein Value Object?
 * - Immutable (keine Setter)
 * - Gleichheit über Wert, nicht Referenz
 * - Keine eigene Identität
 * - Kleine, wiederverwendbare Objekte
 */
public final class UserId {

    private final String value;

    /**
     * Privater Konstruktor - Verwendung über Factory-Methoden
     *
     * Warum privat? Erzwingt Validierung durch Factory-Methoden
     */
    private UserId(String value) {
        // Validierung: Null-Check
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }
        this.value = value;
    }

    /**
     * Erstellt UserId aus existierendem String
     *
     * Wird verwendet in: Deserialisierung aus DB, REST-Requests
     *
     * @param value UUID als String
     * @return UserId Value Object
     */
    public static UserId of(String value) {
        return new UserId(value);
    }

    /**
     * Erstellt UserId aus UUID
     *
     * Wird verwendet in: Wenn UUID-Objekt bereits vorhanden
     *
     * @param uuid UUID-Objekt
     * @return UserId Value Object
     */
    public static UserId of(UUID uuid) {
        return new UserId(uuid.toString());
    }

    /**
     * Generiert neue zufällige UserId
     *
     * Wird verwendet in: RegisterUserService bei User-Erstellung
     * Warum UUID? Garantiert global eindeutig, keine DB-Sequenz nötig
     *
     * @return Neue UserId mit zufälliger UUID
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    /**
     * Gibt den rohen ID-Wert zurück
     *
     * Wird verwendet in: Persistierung, API-Responses
     */
    public String getValue() {
        return value;
    }

    /**
     * Gibt ID als UUID-Objekt zurück
     *
     * Wird verwendet in: Wenn UUID-Format benötigt wird
     */
    public UUID asUuid() {
        return UUID.fromString(value);
    }

    /**
     * Value Object Gleichheit über Wert
     *
     * Warum wichtig? Zwei UserIds mit gleichem Wert sind identisch
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    /**
     * HashCode für Collections und Maps
     *
     * Warum wichtig? Ermöglicht UserId als Map-Key oder in Sets
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * String-Repräsentation für Logging
     */
    @Override
    public String toString() {
        return "UserId{" + value + "}";
    }
}
