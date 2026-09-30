package com.enterprisehub.backend.company.domain;

public record CompanyName(String value) {

    private static final int MAX_LENGTH = 150;

    public CompanyName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Company name is required");
        }

        value = value.trim();

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Company name must contain at most 150 characters"
            );
        }
    }

    public static CompanyName of(String value) {
        return new CompanyName(value);
    }
}
