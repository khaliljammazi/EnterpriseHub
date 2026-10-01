package com.enterprisehub.backend.product.infrastructure.persistence;

import com.enterprisehub.backend.product.domain.Product;
import com.enterprisehub.backend.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class JpaProductRepositoryAdapter implements ProductRepository {
    private final SpringDataProductRepository repository;

    @Override
    public Product save(Product product) {
        return repository.save(ProductJpaEntity.fromDomain(product)).toDomain();
    }

    @Override
    public Optional<Product> findByIdAndCompanyId(UUID id, UUID companyId) {
        return repository.findByIdAndCompanyId(id, companyId).map(ProductJpaEntity::toDomain);
    }

    @Override
    public List<Product> findAllByCompanyId(UUID companyId) {
        return repository.findAllByCompanyIdOrderByNameAsc(companyId).stream()
                .map(ProductJpaEntity::toDomain).toList();
    }

    @Override
    public boolean existsByCompanyIdAndSku(UUID companyId, String sku) {
        return repository.existsByCompanyIdAndSkuIgnoreCase(companyId, sku);
    }

    @Override
    public void delete(Product product) {
        repository.delete(ProductJpaEntity.fromDomain(product));
    }
}
