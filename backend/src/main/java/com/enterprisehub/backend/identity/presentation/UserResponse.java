package com.enterprisehub.backend.identity.presentation;

import com.enterprisehub.backend.identity.application.UserResult;
import com.enterprisehub.backend.identity.domain.Role;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        Set<Role> roles,
        boolean enabled,
        Instant createdAt
) {
    public static UserResponse from(UserResult result) {
        return new UserResponse(
                result.id(),
                result.email(),
                result.roles(),
                result.enabled(),
                result.createdAt()
        );
    }
}
