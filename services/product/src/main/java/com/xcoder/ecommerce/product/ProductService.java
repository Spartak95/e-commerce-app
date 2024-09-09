package com.xcoder.ecommerce.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.xcoder.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        List<Integer> productIds = request.stream().map(ProductPurchaseRequest::productId).toList();
        List<Product> storedProducts = productRepository.findAllByIdInOrderById(productIds);

        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exists");
        }

        List<ProductPurchaseRequest> storedRequest = request.stream()
            .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
            .toList();

        List<ProductPurchaseResponse> purchasedProducts = new ArrayList<>();

        for (int i = 0; i < storedProducts.size(); i++) {
            Product product = storedProducts.get(i);
            ProductPurchaseRequest productRequest = storedRequest.get(i);

            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException(
                    "Insufficient stock quantity for product with Id: " + productRequest.productId());
            }

            double newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);

            productRepository.save(product);
            ProductPurchaseResponse productPurchaseResponse =
                productMapper.toProductPurchaseResponse(product, productRequest.quantity());
            purchasedProducts.add(productPurchaseResponse);
        }

        return purchasedProducts;
    }

    public ProductResponse getById(Integer productId) {
        return productRepository.findById(productId)
            .map(productMapper::toProductResponse)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with the Id: " + productId));
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll()
            .stream()
            .map(productMapper::toProductResponse)
            .toList();
    }
}
