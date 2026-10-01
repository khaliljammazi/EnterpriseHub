package com.enterprisehub.backend.product.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @Test
    void normalizesSkuAndName() {
        Product product = Product.create(UUID.randomUUID(), UUID.randomUUID(), " sku-01 ",
                " Coffee ", new BigDecimal("4.50"), 10);

        assertThat(product.sku()).isEqualTo("SKU-01");
        assertThat(product.name()).isEqualTo("Coffee");
    }

    @Test
    void rejectsNegativePriceAndStock() {
        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        assertThatThrownBy(() -> Product.create(companyId, userId, "SKU", "Coffee",
                new BigDecimal("-1"), 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Product.create(companyId, userId, "SKU", "Coffee",
                BigDecimal.ONE, -1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void detectsOnlyTheTransitionToLowStock() {
        Product product = Product.create(UUID.randomUUID(), UUID.randomUUID(), "SKU", "Coffee",
                BigDecimal.ONE, 10);

        assertThat(product.update("Coffee", BigDecimal.ONE, 5)).isTrue();
        assertThat(product.update("Coffee", BigDecimal.ONE, 3)).isFalse();
        assertThat(product.isLowStock()).isTrue();
    }
}
