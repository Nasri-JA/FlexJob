package com.flexjob.user.domain.model;

import com.flexjob.user.domain.vo.Email;
import com.flexjob.user.domain.vo.UserId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * User Domain Entity - Kern der Geschäftslogik
 *
 * Wird verwendet in:
 * - UserDomainService für Geschäftslogik-Validierung
 * - RegisterUserService für User-Erstellung
 * - UserPersistenceAdapter für Persistierung
 *
 * Zweck:
 * Repräsentiert die zentrale User-Entität mit allen Geschäftsregeln.
 * Diese Klasse ist unabhängig von Framework-Code (kein JPA, keine REST-Annotationen).
 *
 * Warum:
 * - Clean Architecture: Domain-Layer bleibt unabhängig von technischen Details
 * - Geschäftslogik ist testbar ohne Datenbank oder Framework
 * - Value Objects (Email, UserId) erzwingen Konsistenz
 */
@Getter
@Builder
public class User {

    /**
     * Eindeutige User-ID als Value Object
     * Warum Value Object? Typsicherheit und Validierung
     */
    private UserId id;

    /**
     * E-Mail als Value Object mit eingebauter Validierung
     */
    private Email email;

    /**
     * Verschlüsseltes Passwort (nie im Klartext!)
     */
    private String passwordHash;

    /**
     * Benutzertyp (CANDIDATE, EMPLOYER, ADMIN)
     */
    private UserType userType;

    /**
     * Verknüpftes User-Profil (One-to-One Beziehung)
     */
    private UserProfile profile;

    /**
     * Account-Status für Soft-Delete und Aktivierung
     */
    private boolean active;

    /**
     * E-Mail Verifizierungs-Status
     */
    private boolean emailVerified;

    /**
     * Zeitstempel für Audit-Trail
     */
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Aktiviert den User-Account
     *
     * Wird verwendet in: UserDomainService bei Account-Aktivierung
     * Warum eigene Methode? Kapselung der Geschäftslogik
     */
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Deaktiviert den User-Account (Soft-Delete)
     *
     * Wird verwendet in: DeleteUserService
     * Warum Soft-Delete? Daten bleiben für Audit-Zwecke erhalten
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Markiert E-Mail als verifiziert
     *
     * Wird verwendet in: VerifyEmailService
     * Warum wichtig? Verhindert Spam und validiert echte User
     */
    public void verifyEmail() {
        this.emailVerified = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Ändert das Passwort
     *
     * @param newPasswordHash Neuer Passwort-Hash (bereits verschlüsselt!)
     *
     * Wird verwendet in: ChangePasswordService
     * Warum separate Methode? Validierung und Audit-Trail
     */
    public void changePassword(String newPasswordHash) {
        if (newPasswordHash == null || newPasswordHash.isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
        this.passwordHash = newPasswordHash;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Aktualisiert das User-Profil
     *
     * @param profile Neues Profil
     *
     * Wird verwendet in: UpdateProfileService
     */
    public void updateProfile(UserProfile profile) {
        this.profile = profile;
        this.updatedAt = LocalDateTime.now();
    }
}
