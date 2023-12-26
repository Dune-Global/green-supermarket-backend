package com.dune.greensupermarketbackend.cart.service.impl;

import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.cart.CartEntity;
import com.dune.greensupermarketbackend.cart.CartRepository;
import com.dune.greensupermarketbackend.cart.dto.CartDto;
import com.dune.greensupermarketbackend.cart.service.CartService;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public CartDto createCart() {
        CartEntity newCart = new CartEntity();
        CartEntity savedCart = cartRepository.save(newCart);
        CartDto cartDto = mapEntityToDto(savedCart);
        return cartDto;
    }

    private CartDto mapEntityToDto(CartEntity cartEntity) {
        return CartDto.builder()
                .cartId(cartEntity.getCartId())
                .build();
    }
}