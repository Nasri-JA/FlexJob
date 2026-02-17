package com.flexjob.user.application.service;

import com.flexjob.user.domain.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtTokenGenerator - JWT Token Generierung
 *
 * Wird verwendet in:
 * - LoginUserService (nach erfolgreicher Authentifizierung)
 * - RefreshTokenService (beim Token-Refresh)
 *
 * Zweck:
 * Generiert JWT (JSON Web Tokens) für Authentifizierung.
 *
 * Was ist JWT?
 * - JSON Web Token: Standardisiertes Token-Format (RFC 7519)
 * - Besteht aus: Header.Payload.Signature
 * - Selbst-enthaltend: Alle Infos im Token (kein DB-Lookup nötig)
 * - Signiert: Kann nicht manipuliert werden
 *
 * JWT-Struktur:
 * Header: {"alg": "HS256", "typ": "JWT"}
 * Payload: {"sub": "userId", "email": "user@example.com", "roles": ["ROLE_USER"]}
 * Signature: HMACSHA256(base64(header) + "." + base64(payload), secret)
 *
 * Warum JWT?
 * - Stateless: Server muss Session nicht speichern
 * - Skalierbar: Kein shared Session-Storage nötig
 * - Cross-Domain: Token kann in mehreren Services verwendet werden
 * - Mobile-Friendly: Einfacher als Cookies
 */
@Component
public class JwtTokenGenerator {

    /**
     * Secret Key für Token-Signierung
     *
     * WICHTIG:
     * - Muss mindestens 256 Bit (32 Zeichen) sein
     * - In Production: Aus Umgebungsvariable oder Vault laden
     * - NIEMALS im Code committen!
     * - Regelmäßig rotieren
     */
    @Value("${jwt.secret:your-256-bit-secret-key-change-this-in-production}")
    private String secret;

    /**
     * Access Token Lebensdauer in Millisekunden
     * Default: 15 Minuten
     *
     * Warum kurz?
     * - Sicherheit: Kompromittiertes Token schnell ungültig
     * - Wird häufig erneuert via Refresh Token
     */
    @Value("${jwt.access-token-expiration:900000}")
    private Long accessTokenExpiration;

    /**
     * Refresh Token Lebensdauer in Millisekunden
     * Default: 7 Tage
     *
     * Warum länger?
     * - User muss nicht ständig neu einloggen
     * - Kann widerrufen werden (DB-Speicherung)
     */
    @Value("${jwt.refresh-token-expiration:604800000}")
    private Long refreshTokenExpiration;

    /**
     * Generiert Access Token für User
     *
     * Access Token enthält:
     * - subject (sub): User-ID
     * - email: E-Mail-Adresse
     * - userType: CANDIDATE, EMPLOYER, ADMIN
     * - roles: Spring Security Rollen
     * - iat: Issued At (Erstellungszeitpunkt)
     * - exp: Expiration (Ablaufzeitpunkt)
     *
     * Wird verwendet in: Jeder API-Request (Authorization Header)
     */
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail().getValue());
        claims.put("userType", user.getUserType().name());
        claims.put("roles", user.getUserType().getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getId().getValue()) // User-ID als Subject
                .setIssuedAt(new Date()) // Erstellungszeitpunkt
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // HMAC SHA-256
                .compact();
    }

    /**
     * Generiert Refresh Token für User
     *
     * Refresh Token ist minimalistisch:
     * - Nur subject (User-ID)
     * - Längere Lebensdauer
     * - Wird nur für Token-Refresh verwendet
     *
     * Best Practice: In DB speichern für Widerrufung
     */
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().getValue())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Gibt Access Token Ablaufzeit in Sekunden zurück
     *
     * Wird verwendet in: LoginResponse (expiresIn Feld)
     * Client kann Timer starten
     */
    public Long getAccessTokenExpirationSeconds() {
        return accessTokenExpiration / 1000;
    }

    /**
     * Erstellt Signing Key aus Secret
     *
     * HMAC-SHA256 benötigt mindestens 256-Bit Key
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Hinweise zur Token-Verwendung:
     *
     * 1. Client-Side Storage
     *    - Access Token: Memory oder SessionStorage (nicht localStorage!)
     *    - Refresh Token: HttpOnly Cookie (sicherste Option)
     *
     * 2. API-Request
     *    Authorization: Bearer <accessToken>
     *
     * 3. Token-Refresh
     *    - Access Token abgelaufen? -> /auth/refresh mit Refresh Token
     *    - Refresh Token abgelaufen? -> Neu einloggen
     *
     * 4. Security Best Practices
     *    - HTTPS verwenden (Tokens nicht über HTTP!)
     *    - Token nach Logout invalidieren (Blacklist)
     *    - Refresh Token in DB speichern (für Widerrufung)
     *    - XSS-Schutz (keine Tokens in localStorage wenn möglich)
     */
}
