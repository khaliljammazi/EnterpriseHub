package com.enterprisehub.backend.company;

import java.time.Instant;
import java.util.UUID;

public record CompanyResponse(UUID id, String name, CompanyType companyType, Instant createdAt) {

    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getCompanyType(),
                company.getCreatedAt()
        );
    }
}
