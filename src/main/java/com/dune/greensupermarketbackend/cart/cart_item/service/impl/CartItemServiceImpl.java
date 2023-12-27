package com.dune.greensupermarketbackend.cart.cart_item.service.impl;

import com.dune.greensupermarketbackend.cart.CartEntity;
import com.dune.greensupermarketbackend.cart.CartRepository;
import com.dune.greensupermarketbackend.cart.cart_item.CartItemEntity;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemRequestDto;
import com.dune.greensupermarketbackend.cart.cart_item.CartItemRepository;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.cart.cart_item.service.CartItemService;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.product.ProductEntity;
import com.dune.greensupermarketbackend.product.ProductRepository;
import com.dune.greensupermarketbackend.product.dto.ProductDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {
    private CartItemRepository cartItemRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public CartItemResponseDto mapper(CartItemEntity cartItemEntity){
        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setCartItemId(cartItemEntity.getCartItemId());
        cartItemResponseDto.setCartId(cartItemEntity.getCart().getCartId());
        cartItemResponseDto.setProduct(modelMapper.map(cartItemEntity.getProduct(), ProductDto.class));
        cartItemResponseDto.setQuantity(cartItemEntity.getQuantity());
        cartItemResponseDto.setAddedDate(cartItemEntity.getAddedDate());

        return cartItemResponseDto;
    }


    @Override
    public CartItemResponseDto addToCart(CartItemRequestDto cartItemRequestDto) {

        CartEntity cart = cartRepository.findById(cartItemRequestDto.getCartId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Cart not found")
                );

        ProductEntity product = productRepository.findById(cartItemRequestDto.getProductId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Product not found")
                );

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItem.setAddedDate(LocalDateTime.now());

        CartItemEntity savedCartItem = cartItemRepository.save(cartItem);

        return mapper(savedCartItem);
    }

    @Override
    public void removeCartItem(Integer cartItemId) {
        cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Cart item not found")
                );

        cartItemRepository.deleteById(cartItemId);

    }

    @Override
    public List<CartItemResponseDto> getCartItems(Integer cartId) {
        List<CartItemEntity> cartItems =  cartItemRepository.findAllByCartCartId(cartId)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Cart not found")
                );
        return cartItems.stream().map(
                this::mapper
        ).collect(Collectors.toList());
    }
}
