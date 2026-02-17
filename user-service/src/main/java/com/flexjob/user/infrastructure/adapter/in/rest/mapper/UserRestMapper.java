package com.flexjob.user.infrastructure.adapter.in.rest.mapper;

import com.flexjob.user.application.dto.command.LoginCommand;
import com.flexjob.user.application.dto.command.RegisterUserCommand;
import com.flexjob.user.infrastructure.adapter.in.rest.dto.LoginRequest;
import com.flexjob.user.infrastructure.adapter.in.rest.dto.RegisterRequest;
import org.springframework.stereotype.Component;

/**
 * UserRestMapper - Mapper zwischen REST-DTOs und Application-Commands
 *
 * Wird verwendet in:
 * - AuthController (Request -> Command)
 * - UserController (Request -> Command)
 *
 * Zweck:
 * Übersetzt zwischen REST-Layer und Application-Layer.
 * Trennt API-Contracts von interner Struktur.
 *
 * Warum Mapper?
 * - Entkopplung: API kann sich ändern ohne Commands zu ändern
 * - Transformation: API-Naming != Domain-Naming
 * - Anreicherung: Zusätzliche Daten hinzufügen (z.B. Request-ID)
 * - Validierung: Zusätzliche Business-Validierung
 *
 * Alternative: MapStruct
 * - Code-Generierung statt manueller Implementierung
 * - Compile-Time Safety
 * - Performance-optimiert
 *
 * Für einfache Mappings: Manuelle Implementierung ist ausreichend
 * Für komplexe Mappings: MapStruct verwenden
 */
@Component
public class UserRestMapper {

    /**
     * Mapped RegisterRequest zu RegisterUserCommand
     *
     * Transformation:
     * - REST-DTO (Infrastructure) -> Command-DTO (Application)
     * - 1:1 Mapping da Strukturen ähnlich
     *
     * Bei komplexeren Cases:
     * - Request-Metadata hinzufügen (IP, User-Agent)
     * - Default-Werte setzen
     * - Zusätzliche Validierung
     */
    public RegisterUserCommand toRegisterCommand(RegisterRequest request) {
        return RegisterUserCommand.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .userType(request.getUserType())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }

    /**
     * Mapped LoginRequest zu LoginCommand
     *
     * Einfaches 1:1 Mapping
     */
    public LoginCommand toLoginCommand(LoginRequest request) {
        return LoginCommand.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    /**
     * Vorteile von dediziertem Mapper:
     *
     * 1. Single Responsibility
     *    - Controller nur für HTTP-Handling
     *    - Mapper nur für Transformation
     *
     * 2. Testbarkeit
     *    - Mapper-Logik isoliert testbar
     *    - Keine HTTP-Mocks nötig
     *
     * 3. Wiederverwendbarkeit
     *    - Gleicher Mapper für REST + GraphQL
     *    - Konsistente Transformationen
     *
     * 4. Wartbarkeit
     *    - Mapping-Logik an einem Ort
     *    - Einfache Änderungen
     */
}
