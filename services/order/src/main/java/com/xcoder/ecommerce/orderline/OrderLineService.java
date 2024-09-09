package com.xcoder.ecommerce.orderline;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository repository;
    private final OrderLineMapper orderLineMapper;

    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        OrderLine orderLine = orderLineMapper.toOrderLine(orderLineRequest);
        return repository.save(orderLine).getId();
    }

    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return repository.findAllByOrderId(orderId)
            .stream()
            .map(orderLineMapper::toOrderLineResponse)
            .toList();
    }
}
