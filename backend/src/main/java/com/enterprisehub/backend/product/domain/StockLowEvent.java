package com.enterprisehub.backend.product.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record StockLowEvent(
        UUID eventId,
        UUID productId,
        UUID companyId,
        String sku,
        int remainingQuantity,
        Instant occurredAt
) {
    public StockLowEvent {
        Objects.requireNonNull(eventId, "Event id is required");
        Objects.requireNonNull(productId, "Product id is required");
        Objects.requireNonNull(companyId, "Company id is required");
        Objects.requireNonNull(sku, "SKU is required");
        Objects.requireNonNull(occurredAt, "Occurrence date is required");
        if (remainingQuantity < 0) {
            throw new IllegalArgumentException("Remaining quantity must be zero or positive");
        }
    }

    public static StockLowEvent from(Product product) {
        if (!product.isLowStock()) {
            throw new IllegalArgumentException("Product stock is not low");
        }
        return new StockLowEvent(UUID.randomUUID(), product.id(), product.companyId(), product.sku(),
                product.stockQuantity(), Instant.now());
    }
}
