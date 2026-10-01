package com.enterprisehub.backend.product.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findByIdAndCompanyId(UUID id, UUID companyId);
    List<Product> findAllByCompanyId(UUID companyId);
    boolean existsByCompanyIdAndSku(UUID companyId, String sku);
    void delete(Product product);
}
