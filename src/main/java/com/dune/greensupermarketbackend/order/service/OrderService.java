package com.dune.greensupermarketbackend.order.service;

import com.dune.greensupermarketbackend.order.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrderStatus(Integer orderId, OrderDto orderDto);
    OrderDto updatePaymentStatus(Integer orderId, OrderDto orderDto);
    List<OrderDto> findByOrderStatus(String orderStatus);
    List<OrderDto> findByCustomerId(Integer customerId);

    OrderDto payementSuccess(Integer orderId);
    List<OrderDto> getAllOrders();
}
