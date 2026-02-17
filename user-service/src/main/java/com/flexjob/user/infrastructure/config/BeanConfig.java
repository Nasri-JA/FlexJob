package com.flexjob.user.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * BeanConfig - Allgemeine Spring Bean Konfiguration
 *
 * Wird verwendet in:
 * - Spring Boot Auto-Configuration
 * - Dependency Injection Container
 *
 * Zweck:
 * Definiert wiederverwendbare Beans für die Anwendung.
 * Beans sind Singleton-Objekte die von Spring verwaltet werden.
 *
 * Was ist ein Bean?
 * - Objekt das von Spring IoC-Container verwaltet wird
 * - Singleton (eine Instanz für ganze Anwendung)
 * - Kann in andere Beans injiziert werden
 * - Lifecycle von Spring gesteuert
 *
 * Warum @Configuration?
 * - Markiert Klasse als Bean-Definition-Source
 * - @Bean-Methoden werden von Spring aufgerufen
 * - Beans werden beim Startup erstellt
 */
@Configuration
public class BeanConfig {

    /**
     * ObjectMapper Bean - Jackson JSON Serialisierung
     *
     * Wird verwendet in:
     * - REST Controllers (automatisch)
     * - UserEventPublisher (manuell)
     * - Überall wo JSON-Serialisierung benötigt wird
     *
     * Zweck:
     * Konfiguriert JSON-Serialisierung für die gesamte Anwendung.
     *
     * Jackson ObjectMapper:
     * - Java Objects <-> JSON
     * - Automatisch in Spring Boot
     * - Konfigurierbar für Custom-Verhalten
     *
     * Konfigurationen:
     * 1. JavaTimeModule: LocalDateTime, LocalDate serialisieren
     * 2. WRITE_DATES_AS_TIMESTAMPS: false = ISO-8601 Format
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // ==========================================
        // Java 8 Date/Time API Support
        // ==========================================
        // Ohne Module: LocalDateTime wird als Array serialisiert [2025,2,17,10,30,0]
        // Mit Module: LocalDateTime wird als String "2025-02-17T10:30:00"
        mapper.registerModule(new JavaTimeModule());

        // ==========================================
        // Datum-Format Konfiguration
        // ==========================================
        // false = ISO-8601 Format: "2025-02-17T10:30:00Z"
        // true = Unix Timestamp: 1708166400000
        // ISO-8601 ist human-readable und Standard in REST-APIs
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // ==========================================
        // Weitere nützliche Konfigurationen:
        // ==========================================

        // Ignoriere unbekannte Properties beim Deserialisieren
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Ignoriere null-Werte beim Serialisieren
        // mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Pretty-Print für Development (nicht in Production!)
        // mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Snake-Case statt CamelCase (z.B. "user_name" statt "userName")
        // mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        return mapper;
    }

    /**
     * Weitere mögliche Beans:
     *
     * // RestTemplate für HTTP-Requests an andere Services
     * @Bean
     * public RestTemplate restTemplate() {
     *     return new RestTemplate();
     * }
     *
     * // WebClient für reaktive HTTP-Requests
     * @Bean
     * public WebClient webClient() {
     *     return WebClient.builder().build();
     * }
     *
     * // Clock für testbare Zeitstempel
     * @Bean
     * public Clock clock() {
     *     return Clock.systemDefaultZone();
     * }
     *
     * // ModelMapper für Object-Mapping
     * @Bean
     * public ModelMapper modelMapper() {
     *     return new ModelMapper();
     * }
     *
     * // Validator für Custom-Validierung
     * @Bean
     * public Validator validator() {
     *     return new LocalValidatorFactoryBean();
     * }
     */

    /**
     * Bean-Lifecycle:
     *
     * 1. Spring Boot startet
     * 2. @Configuration-Klassen werden gescannt
     * 3. @Bean-Methoden werden aufgerufen
     * 4. Beans werden im ApplicationContext registriert
     * 5. Dependency Injection: Beans werden in andere Beans injiziert
     * 6. @PostConstruct-Methoden werden aufgerufen
     * 7. Anwendung läuft
     * 8. Shutdown: @PreDestroy-Methoden werden aufgerufen
     *
     * Bean-Scopes:
     * - Singleton (default): Eine Instanz für ganze Anwendung
     * - Prototype: Neue Instanz bei jeder Injection
     * - Request: Eine Instanz pro HTTP-Request (Web)
     * - Session: Eine Instanz pro HTTP-Session (Web)
     */
}
