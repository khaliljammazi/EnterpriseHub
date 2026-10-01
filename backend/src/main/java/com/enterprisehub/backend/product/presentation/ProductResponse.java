package com.enterprisehub.backend.product.presentation;

import com.enterprisehub.backend.product.application.ProductResult;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(UUID id, UUID companyId, UUID createdByUserId, String sku, String name,
                              BigDecimal price, int stockQuantity, Instant createdAt) {
    static ProductResponse from(ProductResult result) {
        return new ProductResponse(result.id(), result.companyId(), result.createdByUserId(), result.sku(),
                result.name(), result.price(), result.stockQuantity(), result.createdAt());
    }
}
