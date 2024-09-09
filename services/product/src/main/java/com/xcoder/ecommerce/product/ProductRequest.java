package com.xcoder.ecommerce.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
    Integer id,
    @NotNull(message = "Product name is required")
    String name,
    @NotNull(message = "Product description is required")
    String description,
    @Positive(message = "Available quantity should be positive")
    double availableQuantity,
    @Positive(message = "Available price should be positive")
    BigDecimal price,
    @NotNull(message = "Price category is required")
    Integer categoryId
) {
}
