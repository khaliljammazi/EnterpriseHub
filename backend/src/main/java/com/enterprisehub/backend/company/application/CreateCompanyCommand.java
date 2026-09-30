package com.enterprisehub.backend.company.application;

import com.enterprisehub.backend.company.domain.CompanyType;

public record CreateCompanyCommand(String name, CompanyType companyType) {
}
