package com.dune.greensupermarketbackend.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Integer orderId;
    private Integer customerId;
    private String orderDate;
    private Double shippingFee;
    private Integer numberOfItems;
    private Double totalAmount;
    private String orderStatus;
    private String paymentMode;
    private String paymentStatus;
    private Integer billingAddressId;
    private Integer shippingAddressId;
}
