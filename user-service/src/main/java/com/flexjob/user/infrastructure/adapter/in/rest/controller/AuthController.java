package com.flexjob.user.infrastructure.adapter.in.rest.controller;

import com.flexjob.user.application.dto.command.LoginCommand;
import com.flexjob.user.application.dto.command.RegisterUserCommand;
import com.flexjob.user.application.dto.response.LoginResponse;
import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.application.port.in.LoginUserUseCase;
import com.flexjob.user.application.port.in.RegisterUserUseCase;
import com.flexjob.user.infrastructure.adapter.in.rest.dto.LoginRequest;
import com.flexjob.user.infrastructure.adapter.in.rest.dto.RegisterRequest;
import com.flexjob.user.infrastructure.adapter.in.rest.mapper.UserRestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * AuthController - REST Controller für Authentifizierung
 *
 * Wird verwendet in:
 * - Frontend/Mobile App für User-Registrierung und Login
 * - API-Gateway für Routing
 *
 * Zweck:
 * Bietet REST-Endpoints für Authentifizierung (Register, Login, Refresh).
 * Adapter zwischen HTTP-Welt und Application Core (Hexagonal Architecture).
 *
 * Was ist ein Adapter?
 * - Teil der Infrastructure-Schicht
 * - Übersetzt zwischen externes Protokoll (HTTP/REST) und internen Use Cases
 * - Mapping: REST-DTO -> Command -> Use Case -> Response -> REST-DTO
 *
 * Warum Adapter-Pattern?
 * - Entkopplung: Application Core kennt kein HTTP
 * - Austauschbar: Könnte GraphQL-, gRPC- oder SOAP-Adapter werden
 * - Testbar: Use Cases unabhängig von REST testbar
 *
 * REST-Endpoints:
 * POST /auth/register - Neuen User registrieren
 * POST /auth/login    - User einloggen (JWT erhalten)
 * POST /auth/refresh  - Access Token erneuern
 * POST /auth/logout   - User ausloggen (Token invalidieren)
 *
 * HTTP-Flow:
 * 1. Client sendet HTTP Request (JSON)
 * 2. Spring @RequestBody deserialisiert zu DTO
 * 3. @Valid validiert DTO (JSR-303)
 * 4. Controller mapped DTO zu Command
 * 5. Use Case wird aufgerufen
 * 6. Response wird zu REST-DTO gemappt
 * 7. Spring serialisiert zu JSON
 * 8. HTTP Response an Client
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // WICHTIG: In Production auf spezifische Origins einschränken!
public class AuthController {

    /**
     * Use Cases werden als Interfaces injiziert
     * Warum Interface? Dependency Inversion, Testbarkeit
     */
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final UserRestMapper mapper;

    /**
     * POST /auth/register - User-Registrierung
     *
     * Request Body:
     * {
     *   "email": "user@example.com",
     *   "password": "SecurePass123!",
     *   "userType": "CANDIDATE",
     *   "firstName": "Max",
     *   "lastName": "Mustermann"
     * }
     *
     * Response: 201 CREATED
     * {
     *   "id": "uuid",
     *   "email": "user@example.com",
     *   "userType": "CANDIDATE",
     *   "active": true,
     *   "emailVerified": false,
     *   ...
     * }
     *
     * Error Cases:
     * - 400 BAD REQUEST: Validierung fehlgeschlagen
     * - 409 CONFLICT: E-Mail bereits registriert
     * - 500 INTERNAL SERVER ERROR: Unerwarteter Fehler
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        log.info("Registration request received for email: {}", request.getEmail());

        // ==========================================
        // SCHRITT 1: REST-DTO zu Command mappen
        // ==========================================
        // RegisterRequest (REST-Layer) -> RegisterUserCommand (Application-Layer)
        RegisterUserCommand command = mapper.toRegisterCommand(request);

        // ==========================================
        // SCHRITT 2: Use Case aufrufen
        // ==========================================
        // Use Case führt komplette Business-Logik aus
        UserResponse response = registerUserUseCase.register(command);

        // ==========================================
        // SCHRITT 3: HTTP Response zurückgeben
        // ==========================================
        // 201 CREATED = Ressource wurde erfolgreich erstellt
        // Location Header könnte auf /users/{id} zeigen
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * POST /auth/login - User-Login
     *
     * Request Body:
     * {
     *   "email": "user@example.com",
     *   "password": "SecurePass123!"
     * }
     *
     * Response: 200 OK
     * {
     *   "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *   "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *   "tokenType": "Bearer",
     *   "expiresIn": 900,
     *   "user": { ... }
     * }
     *
     * Error Cases:
     * - 400 BAD REQUEST: Validierung fehlgeschlagen
     * - 401 UNAUTHORIZED: Falsche Credentials
     * - 403 FORBIDDEN: User deaktiviert
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("Login request received for email: {}", request.getEmail());

        // ==========================================
        // SCHRITT 1: REST-DTO zu Command mappen
        // ==========================================
        LoginCommand command = mapper.toLoginCommand(request);

        // ==========================================
        // SCHRITT 2: Use Case aufrufen
        // ==========================================
        LoginResponse response = loginUserUseCase.login(command);

        // ==========================================
        // SCHRITT 3: HTTP Response zurückgeben
        // ==========================================
        // 200 OK = Erfolgreiche Authentifizierung
        return ResponseEntity.ok(response);
    }

    /**
     * Exception-Handling erfolgt in @ControllerAdvice (GlobalExceptionHandler)
     *
     * Vorteile:
     * - Zentrale Fehlerbehandlung
     * - Konsistente Error-Responses
     * - Controller-Code bleibt clean
     *
     * Beispiel GlobalExceptionHandler:
     * @ExceptionHandler(UserNotFoundException.class)
     * ResponseEntity<ErrorResponse> handleUserNotFound() {
     *     return ResponseEntity.status(404).body(...);
     * }
     */

    /**
     * Security-Hinweise:
     *
     * 1. Rate-Limiting (nicht in Controller!)
     *    - Im API-Gateway oder Spring-Rate-Limiter
     *    - Verhindert Brute-Force-Angriffe
     *
     * 2. CORS (Cross-Origin Resource Sharing)
     *    - @CrossOrigin nur für Entwicklung
     *    - Production: WebSecurityConfig mit spezifischen Origins
     *
     * 3. HTTPS
     *    - Nur HTTPS in Production!
     *    - Credentials nie über unverschlüsselte Verbindung
     *
     * 4. Input-Validierung
     *    - @Valid aktiviert JSR-303 Validierung
     *    - DTO-Klassen mit @NotNull, @Email, etc.
     *
     * 5. Logging
     *    - NIEMALS Passwörter loggen!
     *    - Nur E-Mail (oder maskiert)
     *    - Strukturiertes Logging für Monitoring
     */
}
