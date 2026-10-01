package com.enterprisehub.backend.product.infrastructure.persistence;

import com.enterprisehub.backend.product.domain.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
class ProductJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "company_id", nullable = false)
    private UUID companyId;
    @Column(name = "created_by_user_id", nullable = false)
    private UUID createdByUserId;
    @Column(nullable = false, length = 50)
    private String sku;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ProductJpaEntity() {
    }

    private ProductJpaEntity(UUID id, UUID companyId, UUID createdByUserId, String sku, String name,
                             BigDecimal price, int stockQuantity, Instant createdAt) {
        this.id = id;
        this.companyId = companyId;
        this.createdByUserId = createdByUserId;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.createdAt = createdAt;
    }

    static ProductJpaEntity fromDomain(Product product) {
        return new ProductJpaEntity(product.id(), product.companyId(), product.createdByUserId(),
                product.sku(), product.name(), product.price(), product.stockQuantity(), product.createdAt());
    }

    Product toDomain() {
        return Product.rehydrate(id, companyId, createdByUserId, sku, name, price, stockQuantity, createdAt);
    }
}
