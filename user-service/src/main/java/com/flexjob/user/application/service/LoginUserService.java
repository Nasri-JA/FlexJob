package com.flexjob.user.application.service;

import com.flexjob.user.application.dto.command.LoginCommand;
import com.flexjob.user.application.dto.response.LoginResponse;
import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.application.port.in.LoginUserUseCase;
import com.flexjob.user.application.port.out.LoadUserPort;
import com.flexjob.user.domain.exception.UserNotFoundException;
import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.service.UserDomainService;
import com.flexjob.user.domain.vo.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LoginUserService - Application Service für User-Login
 *
 * Wird verwendet in:
 * - AuthController (über LoginUserUseCase Interface)
 *
 * Zweck:
 * Implementiert den kompletten Login-Workflow mit JWT-Token-Generierung.
 *
 * SCHRITT-FÜR-SCHRITT ABLAUF:
 * 1. E-Mail validieren und User laden
 * 2. Passwort verifizieren (BCrypt)
 * 3. User-Status prüfen (aktiv?)
 * 4. JWT Access + Refresh Token generieren
 * 5. LoginResponse mit Tokens zurückgeben
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // Read-Only für bessere Performance
public class LoginUserService implements LoginUserUseCase {

    private final LoadUserPort loadUserPort;
    private final UserDomainService domainService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Override
    public LoginResponse login(LoginCommand command) {
        log.info("Login attempt for email: {}", command.getEmail());

        // ==========================================
        // SCHRITT 1: E-Mail validieren
        // ==========================================
        Email email = Email.of(command.getEmail());

        // ==========================================
        // SCHRITT 2: User laden
        // ==========================================
        User user = loadUserPort.loadByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found for email: {}", email.getMasked());
                    // WICHTIG: Gleiche Fehlermeldung wie bei falschem Passwort
                    // Verhindert User-Enumeration (Angreifer kann nicht testen ob E-Mail existiert)
                    throw new UserNotFoundException("Ungültige E-Mail oder Passwort");
                });

        // ==========================================
        // SCHRITT 3: Passwort verifizieren
        // ==========================================
        // BCrypt.matches() vergleicht Klartext mit Hash
        // Langsam = sicher gegen Brute-Force
        if (!passwordEncoder.matches(command.getPassword(), user.getPasswordHash())) {
            log.warn("Login failed: Invalid password for user: {}", user.getId().getValue());
            // Gleiche Fehlermeldung wie bei nicht gefundenem User
            throw new IllegalArgumentException("Ungültige E-Mail oder Passwort");
        }

        // ==========================================
        // SCHRITT 4: User-Status prüfen
        // ==========================================
        domainService.validateUserActive(user);

        log.info("Login successful for user: {}", user.getId().getValue());

        // ==========================================
        // SCHRITT 5: JWT-Tokens generieren
        // ==========================================
        String accessToken = jwtTokenGenerator.generateAccessToken(user);
        String refreshToken = jwtTokenGenerator.generateRefreshToken(user);

        // ==========================================
        // SCHRITT 6: LoginResponse erstellen
        // ==========================================
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenGenerator.getAccessTokenExpirationSeconds())
                .user(mapToUserResponse(user))
                .build();
    }

    /**
     * Mapped User zu UserResponse (ohne Passwort!)
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().getValue())
                .email(user.getEmail().getValue())
                .userType(user.getUserType())
                .active(user.isActive())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .profile(user.getProfile())
                .build();
    }
}
