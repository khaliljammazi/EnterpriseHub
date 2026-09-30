package com.enterprisehub.backend.identity.infrastructure.persistence;

import com.enterprisehub.backend.identity.domain.Email;
import com.enterprisehub.backend.identity.domain.PasswordHash;
import com.enterprisehub.backend.identity.domain.Role;
import com.enterprisehub.backend.identity.domain.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected UserJpaEntity() {
    }

    private UserJpaEntity(
            UUID id,
            String email,
            String passwordHash,
            Set<Role> roles,
            boolean enabled,
            Instant createdAt
    ) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = new HashSet<>(roles);
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    static UserJpaEntity fromDomain(User user) {
        return new UserJpaEntity(
                user.id(),
                user.email().value(),
                user.passwordHash().value(),
                user.roles(),
                user.enabled(),
                user.createdAt()
        );
    }

    User toDomain() {
        return User.rehydrate(
                id,
                Email.of(email),
                new PasswordHash(passwordHash),
                roles,
                enabled,
                createdAt
        );
    }
}
