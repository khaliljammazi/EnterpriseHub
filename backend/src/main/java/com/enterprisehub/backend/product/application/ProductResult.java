package com.enterprisehub.backend.product.application;

import com.enterprisehub.backend.product.domain.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResult(UUID id, UUID companyId, UUID createdByUserId, String sku, String name,
                            BigDecimal price, int stockQuantity, Instant createdAt) {
    public static ProductResult from(Product product) {
        return new ProductResult(product.id(), product.companyId(), product.createdByUserId(),
                product.sku(), product.name(), product.price(), product.stockQuantity(), product.createdAt());
    }
}
