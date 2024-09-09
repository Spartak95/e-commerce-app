package com.xcoder.ecommerce.orderline;

import com.xcoder.ecommerce.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest orderLineRequest) {
        Order order = Order.builder()
            .id(orderLineRequest.orderId())
            .build();

        return OrderLine.builder()
            .id(orderLineRequest.id())
            .quantity(orderLineRequest.quantity())
            .order(order)
            .productId(orderLineRequest.productId())
            .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(orderLine.getId(), orderLine.getQuantity());
    }
}
