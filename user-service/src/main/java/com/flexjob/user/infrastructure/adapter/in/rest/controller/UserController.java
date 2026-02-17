package com.flexjob.user.infrastructure.adapter.in.rest.controller;

import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.application.port.in.GetUserUseCase;
import com.flexjob.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * UserController - REST Controller für User-Verwaltung
 *
 * Wird verwendet in:
 * - Frontend für User-Profil-Anzeige
 * - Admin-Dashboard für User-Management
 *
 * Zweck:
 * Bietet REST-Endpoints für User-Daten (CRUD ohne Create).
 * Create erfolgt über AuthController/register.
 *
 * REST-Endpoints:
 * GET /users/me      - Aktuell eingeloggter User
 * GET /users/{id}    - User anhand ID
 * PUT /users/{id}    - User aktualisieren
 * DELETE /users/{id} - User löschen
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final GetUserUseCase getUserUseCase;

    /**
     * GET /users/me - Aktuell eingeloggter User
     *
     * Verwendung: Profil-Seite, Header mit User-Name
     *
     * Authorization: Beliebiger authentifizierter User
     * User-ID wird aus JWT-Token extrahiert
     *
     * Response: 200 OK
     * {
     *   "id": "uuid",
     *   "email": "user@example.com",
     *   ...
     * }
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Nur authentifizierte User
    public ResponseEntity<UserResponse> getCurrentUser() {
        log.info("Get current user request");

        // Use Case holt User-ID aus SecurityContext
        UserResponse response = getUserUseCase.getCurrentUser();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /users/{id} - User anhand ID abrufen
     *
     * Verwendung: User-Profil anzeigen, Admin-Tools
     *
     * Authorization:
     * - Eigenes Profil: Jeder authentifizierte User
     * - Fremdes Profil: Nur ADMIN (oder öffentliche Profile)
     *
     * Response: 200 OK
     * Error: 404 NOT FOUND wenn User nicht existiert
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        log.info("Get user by ID request: {}", id);

        UserId userId = UserId.of(id);
        UserResponse response = getUserUseCase.getUserById(userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Weitere Endpoints (Implementierung analog):
     *
     * PUT /users/{id} - User aktualisieren
     * DELETE /users/{id} - User löschen (Soft-Delete)
     * GET /users/{id}/profile - Detailliertes Profil
     * PUT /users/{id}/profile - Profil aktualisieren
     * POST /users/{id}/upload-image - Profilbild hochladen
     */

    /**
     * Spring Security Integration:
     *
     * @PreAuthorize wird durch SecurityConfig aktiviert
     * Prüft ob User die nötige Berechtigung hat
     *
     * Beispiele:
     * - @PreAuthorize("isAuthenticated()") - Nur eingeloggte User
     * - @PreAuthorize("hasRole('ADMIN')") - Nur Admins
     * - @PreAuthorize("#id == authentication.principal.userId") - Nur eigener User
     * - @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER')") - Admin oder Employer
     */
}
