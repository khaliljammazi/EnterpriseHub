package com.enterprisehub.backend.company;

import com.enterprisehub.backend.common.ConflictException;
import com.enterprisehub.backend.common.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void createsACompany() {
        when(companyRepository.existsByNameIgnoreCase("EnterpriseHub")).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> {
            Company company = invocation.getArgument(0);
            ReflectionTestUtils.setField(company, "id", UUID.randomUUID());
            return company;
        });

        CompanyResponse response = companyService.create(
                new CreateCompanyRequest(" EnterpriseHub ", CompanyType.STARTUP)
        );

        assertThat(response.name()).isEqualTo("EnterpriseHub");
        assertThat(response.id()).isNotNull();
        assertThat(response.companyType()).isEqualTo(CompanyType.STARTUP);
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    void rejectsADuplicateCompanyName() {
        when(companyRepository.existsByNameIgnoreCase("EnterpriseHub")).thenReturn(true);

        assertThatThrownBy(() -> companyService.create(
                new CreateCompanyRequest("EnterpriseHub", CompanyType.ENTERPRISE)))
                .isInstanceOf(ConflictException.class)
                .hasMessage("A company with this name already exists");
    }

    @Test
    void findsACompanyById() {
        UUID id = UUID.randomUUID();
        Company company = company("EnterpriseHub", CompanyType.ENTERPRISE, id);
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        CompanyResponse response = companyService.findById(id);

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.companyType()).isEqualTo(CompanyType.ENTERPRISE);
    }

    @Test
    void listsCompanies() {
        Company first = company("First", CompanyType.STARTUP, UUID.randomUUID());
        Company second = company("Second", CompanyType.MALL, UUID.randomUUID());
        when(companyRepository.findAll()).thenReturn(List.of(first, second));

        assertThat(companyService.findAll()).extracting(CompanyResponse::name)
                .containsExactly("First", "Second");
    }

    @Test
    void updatesACompany() {
        UUID id = UUID.randomUUID();
        Company company = company("Old name", CompanyType.STARTUP, id);
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));
        when(companyRepository.existsByNameIgnoreCaseAndIdNot("New name", id)).thenReturn(false);

        CompanyResponse response = companyService.update(
                id, new UpdateCompanyRequest(" New name ", CompanyType.GROCERY_STORE));

        assertThat(response.name()).isEqualTo("New name");
        assertThat(response.companyType()).isEqualTo(CompanyType.GROCERY_STORE);
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

        companyService.delete(id);

        org.mockito.Mockito.verify(companyRepository).delete(company);
    }

    private Company company(String name, CompanyType type, UUID id) {
        Company company = new Company(name, type);
        ReflectionTestUtils.setField(company, "id", id);
        return company;
    }
}
