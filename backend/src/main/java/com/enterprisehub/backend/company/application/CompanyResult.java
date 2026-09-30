package com.enterprisehub.backend.company.application;

import com.enterprisehub.backend.company.domain.Company;
import com.enterprisehub.backend.company.domain.CompanyType;

import java.time.Instant;
import java.util.UUID;

public record CompanyResult(UUID id, String name, CompanyType companyType, Instant createdAt) {

    public static CompanyResult from(Company company) {
        return new CompanyResult(
                company.id(),
                company.name().value(),
                company.companyType(),
                company.createdAt()
        );
    }
}
