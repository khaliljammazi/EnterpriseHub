package com.enterprisehub.backend.company.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

    Company save(Company company);

    List<Company> findAll();

    Optional<Company> findById(UUID id);

    boolean existsByName(CompanyName name);

    boolean existsByNameExcludingId(CompanyName name, UUID excludedId);

    void deleteById(UUID id);
}
