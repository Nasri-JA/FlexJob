package com.flexjob.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * UserServiceApplication - Spring Boot Main-Klasse
 *
 * Wird verwendet in:
 * - Application-Startup (main-Methode)
 * - Microservice-Entry-Point
 *
 * Zweck:
 * Startet den User-Service als eigenständige Spring Boot Anwendung.
 *
 * @SpringBootApplication ist Meta-Annotation für:
 * 1. @Configuration - Klasse als Bean-Definition-Quelle
 * 2. @EnableAutoConfiguration - Spring Boot Auto-Configuration
 * 3. @ComponentScan - Scannt Package für @Component, @Service, etc.
 *
 * Was passiert beim Start?
 * 1. main() wird ausgeführt
 * 2. SpringApplication.run() startet Spring Context
 * 3. Auto-Configuration wird angewendet:
 *    - Datasource (H2/PostgreSQL/MySQL)
 *    - JPA/Hibernate
 *    - Web (Tomcat Embedded Server)
 *    - Security (Spring Security)
 *    - Jackson (JSON)
 *    - etc.
 * 4. Component-Scan findet alle Beans:
 *    - Controllers (@RestController)
 *    - Services (@Service)
 *    - Repositories (@Repository)
 *    - Configurations (@Configuration)
 * 5. Dependency Injection wird ausgeführt
 * 6. @PostConstruct-Methoden werden aufgerufen
 * 7. Embedded Tomcat startet (default Port 8080)
 * 8. Anwendung ist bereit für HTTP-Requests
 *
 * Architektur-Übersicht:
 *
 * ┌────────────────────────────────────────────────────────────┐
 * │                    USER-SERVICE                            │
 * │                                                            │
 * │  ┌────────────────────────────────────────────────────┐   │
 * │  │  DOMAIN LAYER (Business-Logik)                    │   │
 * │  │  - User, UserProfile, UserType                    │   │
 * │  │  - Email, UserId (Value Objects)                  │   │
 * │  │  - UserDomainService                              │   │
 * │  │  - UserNotFoundException                          │   │
 * │  └────────────────────────────────────────────────────┘   │
 * │                       ↑                                    │
 * │  ┌────────────────────────────────────────────────────┐   │
 * │  │  APPLICATION LAYER (Use Cases)                    │   │
 * │  │  Ports (Interfaces):                              │   │
 * │  │  - RegisterUserUseCase, LoginUserUseCase          │   │
 * │  │  - LoadUserPort, SaveUserPort, PublishEventPort   │   │
 * │  │  Services (Implementation):                       │   │
 * │  │  - RegisterUserService, LoginUserService          │   │
 * │  │  - GetUserService, JwtTokenGenerator              │   │
 * │  │  DTOs:                                            │   │
 * │  │  - Commands, Responses                            │   │
 * │  └────────────────────────────────────────────────────┘   │
 * │                       ↑                                    │
 * │  ┌────────────────────────────────────────────────────┐   │
 * │  │  INFRASTRUCTURE LAYER (Technical Details)         │   │
 * │  │  Adapters IN (REST):                              │   │
 * │  │  - AuthController, UserController                 │   │
 * │  │  - RegisterRequest, LoginRequest                  │   │
 * │  │  - UserRestMapper                                 │   │
 * │  │  Adapters OUT (Persistence):                      │   │
 * │  │  - UserJpaEntity, UserProfileJpaEntity            │   │
 * │  │  - UserJpaRepository                              │   │
 * │  │  - UserPersistenceAdapter                         │   │
 * │  │  - UserPersistenceMapper                          │   │
 * │  │  Adapters OUT (Messaging):                        │   │
 * │  │  - UserEventPublisher (Kafka)                     │   │
 * │  │  Security:                                        │   │
 * │  │  - SecurityConfig, JwtTokenProvider               │   │
 * │  │  - JwtAuthenticationFilter                        │   │
 * │  │  Configuration:                                   │   │
 * │  │  - BeanConfig                                     │   │
 * │  └────────────────────────────────────────────────────┘   │
 * └────────────────────────────────────────────────────────────┘
 *
 * Clean Architecture Prinzipien:
 * 1. Dependency Rule: Abhängigkeiten zeigen nach INNEN
 *    - Infrastructure -> Application -> Domain
 *    - Domain kennt weder Application noch Infrastructure
 *
 * 2. Ports & Adapters (Hexagonal Architecture)
 *    - Application definiert Ports (Interfaces)
 *    - Infrastructure implementiert Adapters
 *    - Austauschbare Adapter (REST -> GraphQL, JPA -> MongoDB)
 *
 * 3. Separation of Concerns
 *    - Domain: Geschäftslogik, unabhängig von Frameworks
 *    - Application: Use Cases, Orchestrierung
 *    - Infrastructure: Technische Details (REST, DB, Messaging)
 *
 * 4. Testbarkeit
 *    - Domain: Unit-Tests ohne Mocks
 *    - Application: Tests mit gemockten Ports
 *    - Infrastructure: Integration-Tests mit Test-Containers
 *
 * Vorteile dieser Architektur:
 * - Wartbar: Klare Struktur, leicht zu navigieren
 * - Testbar: Jede Schicht isoliert testbar
 * - Flexibel: Framework/DB austauschbar
 * - Skalierbar: Microservice-Ready
 * - Verständlich: Self-Documenting Code
 *
 * Nachteile:
 * - Mehr Klassen (Boilerplate)
 * - Mehr Mapping-Code
 * - Höhere Lernkurve
 * - Aber: Lohnt sich bei wachsenden Projekten!
 *
 * Microservice-Integration:
 * - Service-Discovery: Eureka/Consul
 * - Config-Server: Spring Cloud Config
 * - API-Gateway: Spring Cloud Gateway
 * - Message-Broker: Kafka/RabbitMQ
 * - Monitoring: Prometheus + Grafana
 * - Tracing: Zipkin/Jaeger
 * - Logging: ELK-Stack
 */
@SpringBootApplication
@EnableJpaRepositories // Aktiviert Spring Data JPA Repositories
public class UserServiceApplication {

    /**
     * Main-Methode - Application Entry Point
     *
     * @param args Command-Line-Argumente
     *
     * Start mit Maven: mvn spring-boot:run
     * Start mit JAR: java -jar user-service.jar
     * Start mit IDE: Run main-Methode
     */
    public static void main(String[] args) {
        // Spring Boot Application starten
        SpringApplication.run(UserServiceApplication.class, args);

        // Console-Output bei erfolgreichem Start
        System.out.println("=".repeat(60));
        System.out.println("  USER-SERVICE ERFOLGREICH GESTARTET");
        System.out.println("=".repeat(60));
        System.out.println("  REST-API: http://localhost:8080/api/v1");
        System.out.println("  Swagger-UI: http://localhost:8080/swagger-ui.html");
        System.out.println("  Health: http://localhost:8080/actuator/health");
        System.out.println("=".repeat(60));

        /**
         * Wichtige Endpoints:
         *
         * Authentication:
         * POST /api/v1/auth/register - Neuen User registrieren
         * POST /api/v1/auth/login - User einloggen
         * POST /api/v1/auth/refresh - Token erneuern
         *
         * User-Management:
         * GET /api/v1/users/me - Aktueller User
         * GET /api/v1/users/{id} - User by ID
         * PUT /api/v1/users/{id} - User aktualisieren
         *
         * Monitoring:
         * GET /actuator/health - Health-Check
         * GET /actuator/info - Service-Info
         * GET /actuator/metrics - Metriken
         */
    }

    /**
     * Configuration-Properties (application.yml):
     *
     * server:
     *   port: 8080
     *
     * spring:
     *   application:
     *     name: user-service
     *
     *   datasource:
     *     url: jdbc:postgresql://localhost:5432/flexjob_users
     *     username: postgres
     *     password: postgres
     *
     *   jpa:
     *     hibernate:
     *       ddl-auto: validate
     *     show-sql: false
     *
     *   kafka:
     *     bootstrap-servers: localhost:9092
     *
     * jwt:
     *   secret: your-256-bit-secret-key
     *   access-token-expiration: 900000
     *   refresh-token-expiration: 604800000
     *
     * logging:
     *   level:
     *     com.flexjob: DEBUG
     *     org.springframework: INFO
     */

    /**
     * Docker-Deployment:
     *
     * Dockerfile:
     * FROM openjdk:17-alpine
     * COPY target/user-service.jar app.jar
     * EXPOSE 8080
     * ENTRYPOINT ["java", "-jar", "/app.jar"]
     *
     * docker-compose.yml:
     * services:
     *   user-service:
     *     build: .
     *     ports:
     *       - "8080:8080"
     *     environment:
     *       SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/flexjob
     *       SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
     *     depends_on:
     *       - postgres
     *       - kafka
     *
     *   postgres:
     *     image: postgres:15
     *     environment:
     *       POSTGRES_DB: flexjob_users
     *       POSTGRES_USER: postgres
     *       POSTGRES_PASSWORD: postgres
     *
     *   kafka:
     *     image: confluentinc/cp-kafka:latest
     */
}
