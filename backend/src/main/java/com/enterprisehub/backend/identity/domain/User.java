package com.enterprisehub.backend.identity.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class User {

    private final UUID id;
    private final Email email;
    private final PasswordHash passwordHash;
    private final Set<Role> roles;
    private final boolean enabled;
    private final Instant createdAt;

    private User(
            UUID id,
            Email email,
            PasswordHash passwordHash,
            Set<Role> roles,
            boolean enabled,
            Instant createdAt
    ) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("At least one role is required");
        }

        this.id = id;
        this.email = Objects.requireNonNull(email, "Email is required");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash is required");
        this.roles = Set.copyOf(roles);
        this.enabled = enabled;
        this.createdAt = Objects.requireNonNull(createdAt, "Creation date is required");
    }

    public static User register(Email email, PasswordHash passwordHash, Set<Role> roles) {
        return new User(null, email, passwordHash, roles, true, Instant.now());
    }

    public static User rehydrate(
            UUID id,
            Email email,
            PasswordHash passwordHash,
            Set<Role> roles,
            boolean enabled,
            Instant createdAt
    ) {
        return new User(
                Objects.requireNonNull(id, "User id is required"),
                email,
                passwordHash,
                roles,
                enabled,
                createdAt
        );
    }

    public UUID id() {
        return id;
    }

    public Email email() {
        return email;
    }

    public PasswordHash passwordHash() {
        return passwordHash;
    }

    public Set<Role> roles() {
        return roles;
    }

    public boolean enabled() {
        return enabled;
    }

    public Instant createdAt() {
        return createdAt;
    }
}
