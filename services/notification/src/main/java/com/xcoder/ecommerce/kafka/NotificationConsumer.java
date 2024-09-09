package com.xcoder.ecommerce.kafka;

import static com.xcoder.ecommerce.notification.NotificationType.ORDER_CONFIRMATION;
import static com.xcoder.ecommerce.notification.NotificationType.PAYMENT_CONFIRMATION;

import java.time.LocalDateTime;

import com.xcoder.ecommerce.email.EmailService;
import com.xcoder.ecommerce.kafka.order.OrderConfirmation;
import com.xcoder.ecommerce.kafka.payment.PaymentConfirmation;
import com.xcoder.ecommerce.notification.Notification;
import com.xcoder.ecommerce.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info("Consuming the message from payment-topic topic:: {}", paymentConfirmation);
        Notification notification = Notification.builder()
            .type(PAYMENT_CONFIRMATION)
            .notificationDate(LocalDateTime.now())
            .paymentConfirmation(paymentConfirmation)
            .build();
        notificationRepository.save(notification);

        String customerName = paymentConfirmation.customerFirstname() + " " + paymentConfirmation.customerLastname();
        emailService.sendPaymentSuccessEmail(
            paymentConfirmation.customerEmail(),
            customerName,
            paymentConfirmation.amount(),
            paymentConfirmation.orderReference()
        );
    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderSuccessNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info("Consuming the message from order-topic topic:: {}", orderConfirmation);
        Notification notification = Notification.builder()
            .type(ORDER_CONFIRMATION)
            .notificationDate(LocalDateTime.now())
            .orderConfirmation(orderConfirmation)
            .build();
        notificationRepository.save(notification);

        String customerName = orderConfirmation.customer().firstname() + " " + orderConfirmation.customer().lastname();
        emailService.sendOrderConfirmationEmail(
            orderConfirmation.customer().email(),
            customerName,
            orderConfirmation.totalAmount(),
            orderConfirmation.orderReference(),
            orderConfirmation.products()
        );
    }
}
