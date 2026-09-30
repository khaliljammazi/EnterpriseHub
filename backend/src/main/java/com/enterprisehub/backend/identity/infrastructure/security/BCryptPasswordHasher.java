package com.enterprisehub.backend.identity.infrastructure.security;

import com.enterprisehub.backend.identity.application.PasswordHasher;
import com.enterprisehub.backend.identity.domain.PasswordHash;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class BCryptPasswordHasher implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    @Override
    public PasswordHash hash(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8 || rawPassword.length() > 72) {
            throw new IllegalArgumentException("Password must contain between 8 and 72 characters");
        }
        return new PasswordHash(passwordEncoder.encode(rawPassword));
    }
}
