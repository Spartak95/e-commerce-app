package com.xcoder.ecommerce.payment;

import java.math.BigDecimal;

import com.xcoder.ecommerce.customer.CustomerResponse;
import com.xcoder.ecommerce.order.PaymentMethod;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderReference,
    CustomerResponse customer
) {
}
