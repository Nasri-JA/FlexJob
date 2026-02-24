package com.flexjob.user.infrastructure.config;

import com.flexjob.user.infrastructure.adapter.out.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * SecurityConfig - Spring Security Konfiguration
 *
 * Wird verwendet in:
 * - Spring Boot Auto-Configuration
 * - Alle HTTP-Requests durchlaufen Security-Filter
 *
 * Zweck:
 * Konfiguriert Authentifizierung und Autorisierung für REST-API.
 * JWT-basierte stateless Authentifizierung.
 *
 * Spring Security Konzepte:
 *
 * 1. Authentication (Authentifizierung)
 *    - "Wer bist du?"
 *    - User-Identität feststellen
 *    - JWT-Token validieren
 *
 * 2. Authorization (Autorisierung)
 *    - "Was darfst du?"
 *    - Zugriff auf Ressourcen kontrollieren
 *    - Rollenbasiert (ROLE_USER, ROLE_ADMIN)
 *
 * 3. SecurityFilterChain
 *    - Kette von Security-Filtern
 *    - Jeder Request durchläuft Filter
 *    - Filter prüfen Auth, CSRF, CORS, etc.
 *
 * 4. Stateless Session
 *    - Keine Server-Side Sessions
 *    - JWT-Token enthält alle Infos
 *    - Horizontal skalierbar
 *
 * Security-Flow:
 * 1. Client sendet Request mit Authorization Header
 * 2. JwtAuthenticationFilter extrahiert Token
 * 3. JwtTokenProvider validiert Token
 * 4. Authentication wird in SecurityContext gesetzt
 * 5. @PreAuthorize prüft Berechtigungen
 * 6. Controller-Methode wird ausgeführt
 * 7. Response an Client
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Security-Filter-Chain Konfiguration
     *
     * Definiert:
     * - Welche Endpoints öffentlich/geschützt sind
     * - CORS-Konfiguration
     * - CSRF-Schutz (deaktiviert für REST-API)
     * - Session-Management (stateless)
     * - JWT-Filter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ==========================================
                // CORS aktivieren
                // ==========================================
                // Cross-Origin Resource Sharing
                // Erlaubt Frontend von anderem Domain auf API zuzugreifen
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ==========================================
                // CSRF deaktivieren
                // ==========================================
                // Cross-Site Request Forgery Schutz
                // Nicht nötig für stateless JWT-API
                // CSRF-Token nur für Session-basierte Auth nötig
                .csrf(csrf -> csrf.disable())

                // ==========================================
                // Authorization-Regeln
                // ==========================================
                .authorizeHttpRequests(auth -> auth
                        // Öffentliche Endpoints (kein Login nötig)
                        .requestMatchers("/api/v1/auth/**").permitAll() // Register, Login
                        .requestMatchers("/actuator/**").permitAll()    // Health-Checks
                        .requestMatchers("/swagger-ui/**").permitAll()  // API-Docs
                        .requestMatchers("/v3/api-docs/**").permitAll() // OpenAPI

                        // Admin-Endpoints (nur für ADMIN-Rolle)
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // Alle anderen Endpoints erfordern Authentifizierung
                        .anyRequest().authenticated()
                )

                // ==========================================
                // Session-Management: STATELESS
                // ==========================================
                // Keine Server-Side Sessions
                // Jeder Request muss JWT-Token mitbringen
                // Horizontal skalierbar (kein Session-Store nötig)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ==========================================
                // JWT-Filter hinzufügen
                // ==========================================
                // Filter läuft VOR UsernamePasswordAuthenticationFilter
                // Extrahiert JWT aus Header und setzt Authentication
                .addFilterBefore(
                        jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /**
     * JWT-Authentication-Filter
     *
     * Wird bei jedem Request ausgeführt:
     * 1. Authorization Header lesen
     * 2. JWT-Token extrahieren
     * 3. Token validieren
     * 4. Authentication in SecurityContext setzen
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    /**
     * Password-Encoder Bean
     *
     * BCrypt - Adaptive Hashing-Funktion:
     * - Langsam (gegen Brute-Force)
     * - Salt automatisch generiert
     * - Cost-Faktor anpassbar (default: 10)
     *
     * Wird verwendet in:
     * - RegisterUserService (Passwort hashen)
     * - LoginUserService (Passwort vergleichen)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS-Konfiguration
     *
     * Erlaubt Frontend-Zugriff von anderen Domains
     *
     * WICHTIG: In Production auf spezifische Origins einschränken!
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Erlaubte Origins (Frontend-URLs)
        // Production: Nur spezifische URLs!
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // React Dev-Server
                "http://localhost:4200",  // Angular Dev-Server
                "http://localhost:8080"   // Vue Dev-Server
        ));

        // Erlaubte HTTP-Methoden
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Erlaubte Headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With"
        ));

        // Credentials erlauben (Cookies, Authorization Header)
        configuration.setAllowCredentials(true);

        // Max-Age für Preflight-Requests (OPTIONS)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Security Best Practices:
     *
     * 1. HTTPS nur in Production
     *    - Tokens nie über unverschlüsselte Verbindung
     *    - HSTS Header setzen
     *
     * 2. Token-Storage
     *    - Access Token: Memory/SessionStorage
     *    - Refresh Token: HttpOnly Cookie
     *    - NICHT in localStorage (XSS-Risiko)
     *
     * 3. Rate-Limiting
     *    - Im API-Gateway
     *    - Verhindert Brute-Force
     *
     * 4. CORS restriktiv
     *    - Nur bekannte Origins
     *    - Nicht "*" in Production
     *
     * 5. Security Headers
     *    - X-Content-Type-Options: nosniff
     *    - X-Frame-Options: DENY
     *    - Content-Security-Policy
     *
     * 6. Error-Messages
     *    - Keine Details über User-Existenz
     *    - Gleiche Nachricht bei falschem User/Passwort
     *
     * 7. Logging
     *    - Login-Versuche loggen
     *    - Fehlgeschlagene Auth loggen
     *    - Monitoring und Alerting
     */
}
