package com.dune.greensupermarketbackend.cart;

import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.config.CustomDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartWithItemsDto {
    private Integer cartId;
    private Integer numberOfItems;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double totalAmount;
    private List<CartItemResponseDto> cartItems;
}
