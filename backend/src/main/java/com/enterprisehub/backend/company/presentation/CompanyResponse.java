package com.enterprisehub.backend.company.presentation;

import com.enterprisehub.backend.company.application.CompanyResult;
import com.enterprisehub.backend.company.domain.CompanyType;

import java.time.Instant;
import java.util.UUID;

public record CompanyResponse(UUID id, String name, CompanyType companyType, Instant createdAt) {

    static CompanyResponse from(CompanyResult result) {
        return new CompanyResponse(
                result.id(),
                result.name(),
                result.companyType(),
                result.createdAt()
        );
    }
}
