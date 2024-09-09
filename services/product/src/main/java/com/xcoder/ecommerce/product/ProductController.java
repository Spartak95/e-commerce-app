package com.xcoder.ecommerce.product;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Integer> createProduct(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
        @RequestBody List<ProductPurchaseRequest> request) {
        return ResponseEntity.ok(productService.purchaseProducts(request));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable("product-id") Integer productId) {
        return ResponseEntity.ok(productService.getById(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }
}
