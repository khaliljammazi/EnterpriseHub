package com.enterprisehub.backend.identity.presentation;

import com.enterprisehub.backend.identity.application.RegisterUserCommand;
import com.enterprisehub.backend.identity.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RegisterUserRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        @Size(max = 254, message = "Email must contain at most 254 characters")
        String email,
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must contain between 8 and 72 characters")
        String password,
        @NotEmpty(message = "At least one role is required")
        Set<Role> roles
) {
    RegisterUserCommand toCommand() {
        return new RegisterUserCommand(email, password, roles);
    }
}
