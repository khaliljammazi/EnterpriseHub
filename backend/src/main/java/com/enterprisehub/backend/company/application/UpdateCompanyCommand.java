package com.enterprisehub.backend.company.application;

import com.enterprisehub.backend.company.domain.CompanyType;

public record UpdateCompanyCommand(String name, CompanyType companyType) {
}
