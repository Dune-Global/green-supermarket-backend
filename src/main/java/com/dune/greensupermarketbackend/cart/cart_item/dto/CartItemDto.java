package com.dune.greensupermarketbackend.cart.cart_item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Integer cartId;
    private Integer quantity;
    private LocalDateTime addedDate;
}
