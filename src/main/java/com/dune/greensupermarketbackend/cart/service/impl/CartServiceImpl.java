package com.dune.greensupermarketbackend.cart.service.impl;

import com.dune.greensupermarketbackend.customer.CustomerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.cart.CartEntity;
import com.dune.greensupermarketbackend.cart.CartRepository;
import com.dune.greensupermarketbackend.cart.CartDto;
import com.dune.greensupermarketbackend.cart.service.CartService;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private ModelMapper modelMapper;

    public CartServiceImpl(CartRepository cartRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDto createCart(CustomerEntity customer) {
        CartEntity newCart = new CartEntity();
        CustomerEntity customerEntity = modelMapper.map(customer, CustomerEntity.class);
        newCart.setCustomer(customerEntity);
        CartDto cartDto = modelMapper.map(cartRepository.save(newCart), CartDto.class);
        return cartDto;
    }
}