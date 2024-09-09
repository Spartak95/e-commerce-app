package com.xcoder.ecommerce.payment;

import com.xcoder.ecommerce.notification.NotificationProducer;
import com.xcoder.ecommerce.notification.PaymentNotificationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentMapper mapper;
    private final PaymentRepository repository;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(@Valid PaymentRequest request) {
        Payment payment = repository.save(mapper.toPayment(request));
        notificationProducer.sendNotification(new PaymentNotificationRequest(
            request.orderReference(),
            request.amount(),
            request.paymentMethod(),
            request.customer().firstname(),
            request.customer().lastname(),
            request.customer().email()
        ));
        return payment.getId();
    }
}
