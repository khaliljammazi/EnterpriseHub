package com.enterprisehub.backend.product.presentation;

import com.enterprisehub.backend.product.application.CreateProductCommand;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank @Size(max = 50) String sku,
        @NotBlank @Size(max = 150) String name,
        @NotNull @DecimalMin(value = "0.00") BigDecimal price,
        @PositiveOrZero int stockQuantity
) {
    CreateProductCommand toCommand() {
        return new CreateProductCommand(sku, name, price, stockQuantity);
    }
}
