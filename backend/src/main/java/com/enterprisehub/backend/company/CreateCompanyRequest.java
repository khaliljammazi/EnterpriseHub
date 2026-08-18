package com.enterprisehub.backend.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCompanyRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 150, message = "Company name must contain at most 150 characters")
        String name,
        @NotNull(message = "Company type is required")
        CompanyType companyType
) {
}
