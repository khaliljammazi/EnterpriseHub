package com.enterprisehub.backend.identity.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void normalizesAValidEmail() {
        Email email = Email.of("  Khalil@EnterpriseHub.COM ");

        assertThat(email.value()).isEqualTo("khalil@enterprisehub.com");
    }

    @Test
    void rejectsAnInvalidEmail() {
        assertThatThrownBy(() -> Email.of("not-an-email"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email format is invalid");
    }
}
