package com.dune.greensupermarketbackend.order.service;

import com.dune.greensupermarketbackend.order.dto.OrderDto;
import com.dune.greensupermarketbackend.order.dto.OrderResponseDto;
import com.dune.greensupermarketbackend.order.dto.OrderWithItemsDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderDto orderDto);
    OrderResponseDto updateOrderStatus(Integer orderId, OrderDto orderDto);
    OrderResponseDto updatePaymentStatus(Integer orderId, OrderDto orderDto);
    List<OrderResponseDto> findByOrderStatus(String orderStatus);
    List<OrderResponseDto> findByCustomerId(Integer customerId);

    OrderResponseDto payementSuccess(Integer orderId);
    List<OrderResponseDto> getAllOrders();
    OrderResponseDto getOrderWithItems(Integer orderId);
}
