package com.dune.greensupermarketbackend.customer.address;

import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.order.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name="customer_shipping_billing")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String locationName;

    @Column(name="first-name",nullable = false)
    private String firstName;

    @Column(name="last-name",nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @OneToMany(mappedBy = "billingAddress")
    private List<OrderEntity> billingOrders;

    @OneToMany(mappedBy = "shippingAddress")
    private List<OrderEntity> shippingOrders;
}
