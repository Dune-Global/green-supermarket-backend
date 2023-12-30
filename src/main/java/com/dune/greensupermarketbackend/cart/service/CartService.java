package com.dune.greensupermarketbackend.cart.service;

import com.dune.greensupermarketbackend.cart.dto.CartDto;
import com.dune.greensupermarketbackend.cart.dto.CartWithItemsDto;
import com.dune.greensupermarketbackend.customer.CustomerEntity;

public interface CartService {
    CartDto createCart(CustomerEntity customer);
    CartWithItemsDto getCartWithItems(Integer cartId);
}
