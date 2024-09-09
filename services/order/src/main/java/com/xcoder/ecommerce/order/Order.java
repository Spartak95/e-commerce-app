package com.xcoder.ecommerce.order;

import static jakarta.persistence.EnumType.STRING;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.xcoder.ecommerce.orderline.OrderLine;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_order")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue
    private Integer id;
    private String reference;
    private BigDecimal totalAmount;
    @Enumerated(STRING)
    private PaymentMethod paymentMethod;
    private String customerId;
    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines;
    @Column(updatable = false, nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    @CreatedDate
    @Column(insertable = false)
    private LocalDateTime createdAt;
}
