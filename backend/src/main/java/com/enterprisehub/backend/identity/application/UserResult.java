package com.enterprisehub.backend.identity.application;

import com.enterprisehub.backend.identity.domain.Role;
import com.enterprisehub.backend.identity.domain.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResult(
        UUID id,
        String email,
        Set<Role> roles,
        boolean enabled,
        Instant createdAt
) {
    public static UserResult from(User user) {
        return new UserResult(
                user.id(),
                user.email().value(),
                user.roles(),
                user.enabled(),
                user.createdAt()
        );
    }
}
