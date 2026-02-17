package com.flexjob.user.application.service;

import com.flexjob.user.application.dto.response.UserResponse;
import com.flexjob.user.application.port.in.GetUserUseCase;
import com.flexjob.user.application.port.out.LoadUserPort;
import com.flexjob.user.domain.exception.UserNotFoundException;
import com.flexjob.user.domain.model.User;
import com.flexjob.user.domain.service.UserDomainService;
import com.flexjob.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GetUserService - Application Service für User-Abfrage
 *
 * Wird verwendet in:
 * - UserController (über GetUserUseCase Interface)
 *
 * Zweck:
 * Lädt User-Daten und gibt sie als Response-DTO zurück.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GetUserService implements GetUserUseCase {

    private final LoadUserPort loadUserPort;
    private final UserDomainService domainService;

    @Override
    public UserResponse getUserById(UserId userId) {
        log.info("Getting user by ID: {}", userId.getValue());

        // User laden
        User user = loadUserPort.loadById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId.getValue()));

        // Zu Response mappen
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse getCurrentUser() {
        log.info("Getting current authenticated user");

        // User-ID aus Security Context holen
        // Spring Security speichert authentifizierten User im SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userIdString = authentication.getName(); // JWT enthält User-ID im "subject"

        UserId userId = UserId.of(userIdString);

        // User laden und zurückgeben
        return getUserById(userId);
    }

    /**
     * Mapped User zu UserResponse
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
