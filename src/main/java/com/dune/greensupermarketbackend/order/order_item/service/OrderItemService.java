package com.dune.greensupermarketbackend.order.order_item.service;

import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemDto;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.order.order_item.OrderItemDto;

import java.util.List;

public interface OrderItemService {
    OrderItemDto create(Integer orderId, CartItemResponseDto cartItem);
    OrderItemDto getByOrderItemId(Integer orderItemId);
    List<OrderItemDto> getByOrderId(Integer orderId);
}
