package com.dune.greensupermarketbackend.order.service;

import com.dune.greensupermarketbackend.order.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrderStatus(Integer orderId, String orderStatus);
    OrderDto updatePaymentStatus(Integer orderId, String paymentStatus);
    List<OrderDto> findByOrderStatus(String orderStatus);
    List<OrderDto> findByCustomerId(Integer customerId);
}
