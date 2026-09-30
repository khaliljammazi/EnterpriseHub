package com.enterprisehub.backend.identity.application;

import com.enterprisehub.backend.identity.domain.Role;

import java.util.Set;

public record RegisterUserCommand(String email, String password, Set<Role> roles) {
}
