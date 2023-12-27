package com.dune.greensupermarketbackend.cart.cart_item.service;

import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemRequestDto;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;

import java.util.List;

public interface CartItemService {
    CartItemResponseDto addToCart(CartItemRequestDto cartItemRequestDto);
    void removeCartItem(Integer cartItemId);

    List<CartItemResponseDto> getCartItems(Integer cartId);

}
