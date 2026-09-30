package com.enterprisehub.backend.company.infrastructure.persistence;

import com.enterprisehub.backend.company.domain.Company;
import com.enterprisehub.backend.company.domain.CompanyName;
import com.enterprisehub.backend.company.domain.CompanyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "companies")
class CompanyJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected CompanyJpaEntity() {
    }

    private CompanyJpaEntity(UUID id, String name, CompanyType companyType, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.companyType = companyType;
        this.createdAt = createdAt;
    }

    static CompanyJpaEntity fromDomain(Company company) {
        return new CompanyJpaEntity(
                company.id(),
                company.name().value(),
                company.companyType(),
                company.createdAt()
        );
    }

    Company toDomain() {
        return Company.rehydrate(id, CompanyName.of(name), companyType, createdAt);
    }
}
