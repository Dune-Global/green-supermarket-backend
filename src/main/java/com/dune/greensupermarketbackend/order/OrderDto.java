package com.dune.greensupermarketbackend.order;

import com.dune.greensupermarketbackend.config.CustomDoubleSerializer;
import com.dune.greensupermarketbackend.order.order_item.OrderItemDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Integer orderId;
    private Integer customerId;
    private String orderDate;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double shippingFee;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double discount;
    private Integer numberOfItems;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double totalAmount;
    private String orderStatus;
    private String paymentMode;
    private String paymentStatus;
    private Integer billingAddressId;
    private Integer shippingAddressId;
    private String note;
    private List<OrderItemDto> orderItems;
}
