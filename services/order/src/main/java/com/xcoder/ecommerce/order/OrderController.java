package com.xcoder.ecommerce.order;

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
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Integer> createOrder(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(orderService.findById(orderId));
    }
}
