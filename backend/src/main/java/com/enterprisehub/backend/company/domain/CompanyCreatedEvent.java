package com.enterprisehub.backend.company.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record CompanyCreatedEvent(
        UUID eventId,
        UUID companyId,
        String companyName,
        CompanyType companyType,
        Instant occurredAt
) {
    public CompanyCreatedEvent {
        Objects.requireNonNull(eventId, "Event id is required");
        Objects.requireNonNull(companyId, "Company id is required");
        Objects.requireNonNull(companyName, "Company name is required");
        Objects.requireNonNull(companyType, "Company type is required");
        Objects.requireNonNull(occurredAt, "Occurrence date is required");
    }

    public static CompanyCreatedEvent from(Company company) {
        return new CompanyCreatedEvent(
                UUID.randomUUID(),
                company.id(),
                company.name().value(),
                company.companyType(),
                Instant.now()
        );
    }
}
