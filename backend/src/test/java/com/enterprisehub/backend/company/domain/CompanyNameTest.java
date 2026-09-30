package com.enterprisehub.backend.company.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CompanyNameTest {

    @Test
    void trimsAValidCompanyName() {
        CompanyName companyName = CompanyName.of("  EnterpriseHub  ");

        assertThat(companyName.value()).isEqualTo("EnterpriseHub");
    }

    @Test
    void rejectsABlankCompanyName() {
        assertThatThrownBy(() -> CompanyName.of("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Company name is required");
    }

    @Test
    void rejectsACompanyNameLongerThan150Characters() {
        String tooLongName = "a".repeat(151);

        assertThatThrownBy(() -> CompanyName.of(tooLongName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Company name must contain at most 150 characters");
    }
}
