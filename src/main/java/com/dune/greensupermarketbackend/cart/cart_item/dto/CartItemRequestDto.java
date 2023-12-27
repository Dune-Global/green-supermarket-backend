package com.dune.greensupermarketbackend.cart.cart_item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDto extends CartItemDto{
    private Integer productId;
}
