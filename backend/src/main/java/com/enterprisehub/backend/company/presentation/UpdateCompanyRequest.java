package com.enterprisehub.backend.company.presentation;

import com.enterprisehub.backend.company.application.UpdateCompanyCommand;
import com.enterprisehub.backend.company.domain.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCompanyRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 150, message = "Company name must contain at most 150 characters")
        String name,
        @NotNull(message = "Company type is required")
        CompanyType companyType
) {
    UpdateCompanyCommand toCommand() {
        return new UpdateCompanyCommand(name, companyType);
    }
}
