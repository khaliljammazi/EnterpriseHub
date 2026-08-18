package com.enterprisehub.backend.company;

import com.enterprisehub.backend.common.ConflictException;
import com.enterprisehub.backend.common.NotFoundException;
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
    public CompanyResponse create(CreateCompanyRequest request) {
        String name = request.name().trim();
        if (companyRepository.existsByNameIgnoreCase(name)) {
            throw new ConflictException("A company with this name already exists");
        }
        Company company = new Company(name, request.companyType());
        return CompanyResponse.from(companyRepository.save(company));
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> findAll() {
        return companyRepository.findAll().stream()
                .map(CompanyResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CompanyResponse findById(UUID id) {
        return CompanyResponse.from(findCompany(id));
    }

    @Transactional
    public CompanyResponse update(UUID id, UpdateCompanyRequest request) {
        Company company = findCompany(id);
        String name = request.name().trim();
        if (companyRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new ConflictException("A company with this name already exists");
        }
        company.update(name, request.companyType());
        return CompanyResponse.from(company);
    }

    @Transactional
    public void deleteCompany(UUID id) {
        Company company = findCompany(id);
        companyRepository.delete(company);
    }

    private Company findCompany(UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found: " + id));
    }
}
