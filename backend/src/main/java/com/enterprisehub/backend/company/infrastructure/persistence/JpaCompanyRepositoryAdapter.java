package com.enterprisehub.backend.company.infrastructure.persistence;

import com.enterprisehub.backend.company.domain.Company;
import com.enterprisehub.backend.company.domain.CompanyName;
import com.enterprisehub.backend.company.domain.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class JpaCompanyRepositoryAdapter implements CompanyRepository {

    private final SpringDataCompanyRepository repository;

    @Override
    public Company save(Company company) {
        return repository.save(CompanyJpaEntity.fromDomain(company)).toDomain();
    }

    @Override
    public List<Company> findAll() {
        return repository.findAll().stream()
                .map(CompanyJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return repository.findById(id).map(CompanyJpaEntity::toDomain);
    }

    @Override
    public boolean existsByName(CompanyName name) {
        return repository.existsByNameIgnoreCase(name.value());
    }

    @Override
    public boolean existsByNameExcludingId(CompanyName name, UUID excludedId) {
        return repository.existsByNameIgnoreCaseAndIdNot(name.value(), excludedId);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
