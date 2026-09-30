package com.enterprisehub.backend.company.application;

import com.enterprisehub.backend.common.ConflictException;
import com.enterprisehub.backend.common.NotFoundException;
import com.enterprisehub.backend.company.domain.Company;
import com.enterprisehub.backend.company.domain.CompanyName;
import com.enterprisehub.backend.company.domain.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public CompanyResult create(CreateCompanyCommand command) {
        CompanyName name = CompanyName.of(command.name());
        if (companyRepository.existsByName(name)) {
            throw new ConflictException("A company with this name already exists");
        }

        Company company = Company.create(name, command.companyType());
        return CompanyResult.from(companyRepository.save(company));
    }

    @Transactional(readOnly = true)
    public List<CompanyResult> findAll() {
        return companyRepository.findAll().stream()
                .map(CompanyResult::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CompanyResult findById(UUID id) {
        return CompanyResult.from(findCompany(id));
    }

    @Transactional
    public CompanyResult update(UUID id, UpdateCompanyCommand command) {
        Company company = findCompany(id);
        CompanyName name = CompanyName.of(command.name());

        if (companyRepository.existsByNameExcludingId(name, id)) {
            throw new ConflictException("A company with this name already exists");
        }

        company.update(name, command.companyType());
        return CompanyResult.from(companyRepository.save(company));
    }

    @Transactional
    public void deleteCompany(UUID id) {
        findCompany(id);
        companyRepository.deleteById(id);
    }

    private Company findCompany(UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found: " + id));
    }
}
