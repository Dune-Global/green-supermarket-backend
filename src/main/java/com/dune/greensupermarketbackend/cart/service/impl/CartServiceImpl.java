package com.dune.greensupermarketbackend.cart.service.impl;

import com.dune.greensupermarketbackend.cart.CartWithItemsDto;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.cart.cart_item.service.CartItemService;
import com.dune.greensupermarketbackend.customer.CustomerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.cart.CartEntity;
import com.dune.greensupermarketbackend.cart.CartRepository;
import com.dune.greensupermarketbackend.cart.CartDto;
import com.dune.greensupermarketbackend.cart.service.CartService;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final CartItemService cartItemService;

    public CartServiceImpl(CartRepository cartRepository, ModelMapper modelMapper, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.cartItemService = cartItemService;
    }

    @Override
    public CartDto createCart(CustomerEntity customer) {
        CartEntity newCart = new CartEntity();
        CustomerEntity customerEntity = modelMapper.map(customer, CustomerEntity.class);
        newCart.setCustomer(customerEntity);
        return modelMapper.map(cartRepository.save(newCart), CartDto.class);
    }

    @Override
    public CartWithItemsDto getCartWithItems(Integer cartId) {
        CartWithItemsDto cartWithItemsDto = new CartWithItemsDto();
        cartWithItemsDto.setCartId(cartId);
        List<CartItemResponseDto> cartItems = cartItemService.getCartItems(cartId);
        cartWithItemsDto.setCartItems(cartItems);
        cartWithItemsDto.setNumberOfItems(cartItems.size());

        Double totalAmount = 0.0;
        for (CartItemResponseDto cartItem : cartItems) {
            totalAmount += cartItem.getProduct().getCurrentPrice() * cartItem.getQuantity();
        }
        cartWithItemsDto.setTotalAmount(totalAmount);

        return cartWithItemsDto;
    }
}