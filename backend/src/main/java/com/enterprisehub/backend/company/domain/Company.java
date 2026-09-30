package com.enterprisehub.backend.company.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Company {

    private final UUID id;
    private CompanyName name;
    private CompanyType companyType;
    private final Instant createdAt;

    private Company(UUID id, CompanyName name, CompanyType companyType, Instant createdAt) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Company name is required");
        this.companyType = Objects.requireNonNull(companyType, "Company type is required");
        this.createdAt = Objects.requireNonNull(createdAt, "Creation date is required");
    }

    public static Company create(CompanyName name, CompanyType companyType) {
        return new Company(null, name, companyType, Instant.now());
    }

    public static Company rehydrate(
            UUID id,
            CompanyName name,
            CompanyType companyType,
            Instant createdAt
    ) {
        return new Company(
                Objects.requireNonNull(id, "Company id is required"),
                name,
                companyType,
                createdAt
        );
    }

    public void update(CompanyName name, CompanyType companyType) {
        this.name = Objects.requireNonNull(name, "Company name is required");
        this.companyType = Objects.requireNonNull(companyType, "Company type is required");
    }

    public UUID id() {
        return id;
    }

    public CompanyName name() {
        return name;
    }

    public CompanyType companyType() {
        return companyType;
    }

    public Instant createdAt() {
        return createdAt;
    }
}
