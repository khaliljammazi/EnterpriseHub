package com.enterprisehub.backend.company.application;

import com.enterprisehub.backend.common.ConflictException;
import com.enterprisehub.backend.common.NotFoundException;
import com.enterprisehub.backend.company.domain.Company;
import com.enterprisehub.backend.company.domain.CompanyName;
import com.enterprisehub.backend.company.domain.CompanyRepository;
import com.enterprisehub.backend.company.domain.CompanyType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void createsACompany() {
        when(companyRepository.existsByName(CompanyName.of("EnterpriseHub"))).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> {
            Company company = invocation.getArgument(0);
            return Company.rehydrate(
                    UUID.randomUUID(),
                    company.name(),
                    company.companyType(),
                    company.createdAt()
            );
        });

        CompanyResult result = companyService.create(
                new CreateCompanyCommand(" EnterpriseHub ", CompanyType.STARTUP)
        );

        assertThat(result.name()).isEqualTo("EnterpriseHub");
        assertThat(result.id()).isNotNull();
        assertThat(result.companyType()).isEqualTo(CompanyType.STARTUP);
        assertThat(result.createdAt()).isNotNull();
    }

    @Test
    void rejectsADuplicateCompanyName() {
        when(companyRepository.existsByName(CompanyName.of("EnterpriseHub"))).thenReturn(true);

        assertThatThrownBy(() -> companyService.create(
                new CreateCompanyCommand("EnterpriseHub", CompanyType.ENTERPRISE)))
                .isInstanceOf(ConflictException.class)
                .hasMessage("A company with this name already exists");
    }

    @Test
    void findsACompanyById() {
        UUID id = UUID.randomUUID();
        Company company = company("EnterpriseHub", CompanyType.ENTERPRISE, id);
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        CompanyResult result = companyService.findById(id);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.companyType()).isEqualTo(CompanyType.ENTERPRISE);
    }

    @Test
    void listsCompanies() {
        Company first = company("First", CompanyType.STARTUP, UUID.randomUUID());
        Company second = company("Second", CompanyType.MALL, UUID.randomUUID());
        when(companyRepository.findAll()).thenReturn(List.of(first, second));

        assertThat(companyService.findAll()).extracting(CompanyResult::name)
                .containsExactly("First", "Second");
    }

    @Test
    void updatesACompany() {
        UUID id = UUID.randomUUID();
        Company company = company("Old name", CompanyType.STARTUP, id);
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));
        when(companyRepository.existsByNameExcludingId(CompanyName.of("New name"), id))
                .thenReturn(false);
        when(companyRepository.save(company)).thenReturn(company);

        CompanyResult result = companyService.update(
                id,
                new UpdateCompanyCommand(" New name ", CompanyType.GROCERY_STORE)
        );

        assertThat(result.name()).isEqualTo("New name");
        assertThat(result.companyType()).isEqualTo(CompanyType.GROCERY_STORE);
        verify(companyRepository).save(company);
    }

    @Test
    void returnsNotFoundForUnknownCompany() {
        UUID id = UUID.randomUUID();
        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Company not found: " + id);
    }

    @Test
    void deletesACompany() {
        UUID id = UUID.randomUUID();
        Company company = company("EnterpriseHub", CompanyType.STARTUP, id);
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        companyService.deleteCompany(id);

        verify(companyRepository).deleteById(id);
    }

    private Company company(String name, CompanyType type, UUID id) {
        return Company.rehydrate(id, CompanyName.of(name), type, Instant.now());
    }
}
