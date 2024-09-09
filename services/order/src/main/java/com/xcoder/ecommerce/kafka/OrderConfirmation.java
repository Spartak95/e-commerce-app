package com.xcoder.ecommerce.kafka;

import java.math.BigDecimal;
import java.util.List;

import com.xcoder.ecommerce.customer.CustomerResponse;
import com.xcoder.ecommerce.order.PaymentMethod;
import com.xcoder.ecommerce.product.PurchaseResponse;

public record OrderConfirmation(
    String orderReference,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    CustomerResponse customer,
    List<PurchaseResponse> products
) {
}
