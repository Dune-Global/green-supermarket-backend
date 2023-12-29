package com.dune.greensupermarketbackend.order;

import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.address.AddressEntity;
import com.dune.greensupermarketbackend.order.order_item.OrderItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "shipping_fee", nullable = false)
    private Double shippingFee;

    @Column(name = "number_of_items")
    private Integer numberOfItems;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "payment_mode", nullable = false)
    private String paymentMode;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "billing_address_id", nullable = false)
    private AddressEntity billingAddress;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private AddressEntity shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems;

}