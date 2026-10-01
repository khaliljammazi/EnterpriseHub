package com.enterprisehub.backend.product.application;

import java.math.BigDecimal;

public record UpdateProductCommand(String name, BigDecimal price, int stockQuantity) {
}
