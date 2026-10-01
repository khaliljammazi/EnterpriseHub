package com.enterprisehub.backend.product.application;

import java.math.BigDecimal;

public record CreateProductCommand(String sku, String name, BigDecimal price, int stockQuantity) {
}
