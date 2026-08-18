package com.enterprisehub.backend.company;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "companies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Company(String name, CompanyType companyType) {
        this.name = normalizeName(name);
        this.companyType = Objects.requireNonNull(companyType, "Company type is required");
        this.createdAt = Instant.now();
    }

    public void update(String name, CompanyType companyType) {
        this.name = normalizeName(name);
        this.companyType = Objects.requireNonNull(companyType, "Company type is required");
    }

    private static String normalizeName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Company name is required");
        }
        return value.trim();
    }
}
