package com.enterprisehub.backend.product.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Product {

    public static final int LOW_STOCK_THRESHOLD = 5;

    private final UUID id;
    private final UUID companyId;
    private final UUID createdByUserId;
    private String sku;
    private String name;
    private BigDecimal price;
    private int stockQuantity;
    private final Instant createdAt;

    private Product(UUID id, UUID companyId, UUID createdByUserId, String sku, String name,
                    BigDecimal price, int stockQuantity, Instant createdAt) {
        this.id = id;
        this.companyId = Objects.requireNonNull(companyId, "Company id is required");
        this.createdByUserId = Objects.requireNonNull(createdByUserId, "Creator id is required");
        this.sku = validateSku(sku);
        this.name = validateName(name);
        this.price = validatePrice(price);
        this.stockQuantity = validateStock(stockQuantity);
        this.createdAt = Objects.requireNonNull(createdAt, "Creation date is required");
    }

    public static Product create(UUID companyId, UUID createdByUserId, String sku, String name,
                                 BigDecimal price, int stockQuantity) {
        return new Product(null, companyId, createdByUserId, sku, name, price, stockQuantity, Instant.now());
    }

    public static Product rehydrate(UUID id, UUID companyId, UUID createdByUserId, String sku,
                                    String name, BigDecimal price, int stockQuantity, Instant createdAt) {
        return new Product(Objects.requireNonNull(id, "Product id is required"), companyId,
                createdByUserId, sku, name, price, stockQuantity, createdAt);
    }

    public boolean update(String name, BigDecimal price, int stockQuantity) {
        boolean wasLowStock = isLowStock();
        this.name = validateName(name);
        this.price = validatePrice(price);
        this.stockQuantity = validateStock(stockQuantity);
        return !wasLowStock && isLowStock();
    }

    public boolean isLowStock() {
        return stockQuantity <= LOW_STOCK_THRESHOLD;
    }

    private static String validateSku(String value) {
        if (value == null || value.isBlank() || value.trim().length() > 50) {
            throw new IllegalArgumentException("SKU is required and must contain at most 50 characters");
        }
        return value.trim().toUpperCase();
    }

    private static String validateName(String value) {
        if (value == null || value.isBlank() || value.trim().length() > 150) {
            throw new IllegalArgumentException("Product name is required and must contain at most 150 characters");
        }
        return value.trim();
    }

    private static BigDecimal validatePrice(BigDecimal value) {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("Price must be zero or positive");
        }
        return value;
    }

    private static int validateStock(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Stock quantity must be zero or positive");
        }
        return value;
    }

    public UUID id() { return id; }
    public UUID companyId() { return companyId; }
    public UUID createdByUserId() { return createdByUserId; }
    public String sku() { return sku; }
    public String name() { return name; }
    public BigDecimal price() { return price; }
    public int stockQuantity() { return stockQuantity; }
    public Instant createdAt() { return createdAt; }
}
