package com.enterprisehub.backend.product.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringDataProductRepository extends JpaRepository<ProductJpaEntity, UUID> {
    Optional<ProductJpaEntity> findByIdAndCompanyId(UUID id, UUID companyId);
    List<ProductJpaEntity> findAllByCompanyIdOrderByNameAsc(UUID companyId);
    boolean existsByCompanyIdAndSkuIgnoreCase(UUID companyId, String sku);
}
