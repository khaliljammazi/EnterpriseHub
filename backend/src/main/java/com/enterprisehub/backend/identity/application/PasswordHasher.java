package com.enterprisehub.backend.identity.application;

import com.enterprisehub.backend.identity.domain.PasswordHash;

public interface PasswordHasher {

    PasswordHash hash(String rawPassword);
}
