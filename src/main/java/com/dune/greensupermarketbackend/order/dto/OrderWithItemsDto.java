package com.dune.greensupermarketbackend.order.dto;

import com.dune.greensupermarketbackend.order.order_item.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithItemsDto {
    private OrderResponseDto order;
    private List<OrderItemDto> orderItems;
}
