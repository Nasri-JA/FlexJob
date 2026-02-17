package com.flexjob.user.infrastructure.config;

import com.flexjob.user.infrastructure.adapter.out.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtAuthenticationFilter - Spring Security Filter für JWT
 *
 * Wird verwendet in:
 * - SecurityConfig (addFilterBefore)
 * - Jeder HTTP-Request durchläuft diesen Filter
 *
 * Zweck:
 * Extrahiert JWT-Token aus HTTP-Header und authentifiziert User.
 * Setzt Authentication im SecurityContext für @PreAuthorize.
 *
 * Was ist ein Filter?
 * - Teil der Spring Security Filter-Chain
 * - Läuft VOR Controller-Methoden
 * - Kann Request modifizieren oder ablehnen
 * - OncePerRequestFilter: Garantiert einmal pro Request
 *
 * Filter-Flow:
 * Request -> Filter 1 -> Filter 2 -> ... -> JwtAuthenticationFilter
 *   -> Controller -> Response
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Haupt-Filter-Methode
     *
     * Wird bei JEDEM Request ausgeführt:
     * 1. JWT-Token aus Header extrahieren
     * 2. Token validieren
     * 3. Authentication in SecurityContext setzen
     * 4. Request weiterleiten
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // ==========================================
            // SCHRITT 1: JWT-Token aus Header extrahieren
            // ==========================================
            // Header: "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            String token = extractTokenFromRequest(request);

            // ==========================================
            // SCHRITT 2: Token validieren
            // ==========================================
            // Prüft Signatur, Ablaufzeit, Format
            if (token != null && jwtTokenProvider.validateToken(token)) {

                // ==========================================
                // SCHRITT 3: Authentication erstellen
                // ==========================================
                // Authentication-Objekt aus Token-Claims
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // ==========================================
                // SCHRITT 4: Authentication in SecurityContext setzen
                // ==========================================
                // SecurityContext ist Thread-Local Storage
                // Controller kann auf authenticated User zugreifen
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT authentication successful for user: {}",
                        authentication.getName());
            }

        } catch (Exception e) {
            // Token-Validierung fehlgeschlagen
            log.error("JWT authentication failed: {}", e.getMessage());
            // SecurityContext bleibt leer -> User ist nicht authentifiziert
            // Request wird trotzdem weitergeleitet (Controller entscheidet über 401)
        }

        // ==========================================
        // SCHRITT 5: Request weiterleiten
        // ==========================================
        // Nächster Filter in der Chain oder Controller
        filterChain.doFilter(request, response);
    }

    /**
     * Extrahiert JWT-Token aus Authorization Header
     *
     * Authorization Header Format:
     * "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     *
     * @param request HTTP-Request
     * @return JWT-Token (ohne "Bearer " Prefix) oder null
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        // Header lesen
        String bearerToken = request.getHeader("Authorization");

        // Prüfen: Nicht leer und startet mit "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // "Bearer " entfernen (7 Zeichen)
            return bearerToken.substring(7);
        }

        return null;
    }

    /**
     * Überschreibung: Filter auch für Error-Requests ausführen?
     *
     * false = Filter nur für normale Requests
     * true = Filter auch für /error Endpoint
     *
     * Default: false (ausreichend für JWT)
     */
    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    /**
     * Security-Flow Beispiel:
     *
     * 1. Client: POST /api/v1/auth/login
     *    Body: { "email": "...", "password": "..." }
     *    -> LoginUserService gibt JWT-Token zurück
     *
     * 2. Client speichert Token (Memory/SessionStorage)
     *
     * 3. Client: GET /api/v1/users/me
     *    Header: Authorization: Bearer <token>
     *    -> JwtAuthenticationFilter extrahiert Token
     *    -> Token wird validiert
     *    -> Authentication in SecurityContext
     *    -> UserController kann auf authenticated User zugreifen
     *    -> Response mit User-Daten
     *
     * 4. Token abgelaufen?
     *    -> Client: POST /api/v1/auth/refresh
     *       Body: { "refreshToken": "..." }
     *    -> Neuer Access Token wird generiert
     *
     * 5. Logout?
     *    -> Client löscht Token aus Storage
     *    -> Optional: Token in Blacklist auf Server
     */
}
