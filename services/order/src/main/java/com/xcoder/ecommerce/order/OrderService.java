package com.xcoder.ecommerce.order;

import java.util.List;

import com.xcoder.ecommerce.customer.CustomerClient;
import com.xcoder.ecommerce.customer.CustomerResponse;
import com.xcoder.ecommerce.exception.BusinessException;
import com.xcoder.ecommerce.kafka.OrderConfirmation;
import com.xcoder.ecommerce.kafka.OrderProducer;
import com.xcoder.ecommerce.orderline.OrderLineRequest;
import com.xcoder.ecommerce.orderline.OrderLineService;
import com.xcoder.ecommerce.payment.PaymentClient;
import com.xcoder.ecommerce.payment.PaymentRequest;
import com.xcoder.ecommerce.product.ProductClient;
import com.xcoder.ecommerce.product.PurchaseRequest;
import com.xcoder.ecommerce.product.PurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderLineService orderLineService;
    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final OrderProducer orderProducer;
    private final OrderMapper orderMapper;

    public Integer createOrder(OrderRequest request) {
        CustomerResponse customer = this.customerClient.findCustomerById(request.customerId())
            .orElseThrow(() -> new BusinessException("Cannot create order:: No Customer exists with the provided ID"));

        List<PurchaseResponse> purchaseProducts = this.productClient.purchaseResponses(request.products());
        Order order = this.orderRepository.save(orderMapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            OrderLineRequest orderLineRequest = new OrderLineRequest(null, order.getId(),
                                                                     purchaseRequest.productId(),
                                                                     purchaseRequest.quantity());
            orderLineService.saveOrderLine(orderLineRequest);
        }

        PaymentRequest paymentRequest = new PaymentRequest(
            request.amount(),
            request.paymentMethod(),
            order.getId(),
            order.getReference(),
            customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        orderProducer.sendOrderConfirmation(
            new OrderConfirmation(request.reference(),
                                  request.amount(),
                                  request.paymentMethod(),
                                  customer,
                                  purchaseProducts)
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
            .stream()
            .map(orderMapper::fromOrder)
            .toList();
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
            .map(orderMapper::fromOrder)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", orderId)));
    }
}
