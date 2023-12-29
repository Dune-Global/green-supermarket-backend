package com.dune.greensupermarketbackend.cart.cart_item.dto;

import com.dune.greensupermarketbackend.product.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDto extends CartItemDto{
    private Integer cartItemId;
    private ProductDto product;
}
