package com.dune.greensupermarketbackend.cart.cart_item.service.impl;

import com.dune.greensupermarketbackend.cart.CartEntity;
import com.dune.greensupermarketbackend.cart.CartRepository;
import com.dune.greensupermarketbackend.cart.cart_item.CartItemEntity;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemRequestDto;
import com.dune.greensupermarketbackend.cart.cart_item.CartItemRepository;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.cart.cart_item.service.CartItemService;
import com.dune.greensupermarketbackend.discount.DiscountEntity;
import com.dune.greensupermarketbackend.discount.DiscountRepository;
import com.dune.greensupermarketbackend.discount.dto.DiscountDto;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.product.ProductEntity;
import com.dune.greensupermarketbackend.product.ProductRepository;
import com.dune.greensupermarketbackend.product.dto.ProductDto;
import com.dune.greensupermarketbackend.product.dto.ProductResponseDto;
import com.dune.greensupermarketbackend.product.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ModelMapper modelMapper;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository, DiscountRepository discountRepository, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
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

    public Double getDiscountedPrice(ProductDto productDto) {
        Double discountedPrice = null;
        DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(productDto.getProductId());
        if (discountEntity != null) {
            discountedPrice =  productDto.getOriginalPrice() - (productDto.getOriginalPrice() * discountEntity.getRate() / 100);
        }else {
            discountedPrice = productDto.getOriginalPrice();
        }
        return discountedPrice;
    }

    public Double getRate(ProductDto productDto) {
        Double rate = 5.0;
        return rate;
    }

    @Override
    public CartItemResponseDto addToCart(CartItemRequestDto cartItemRequestDto) {
        CartEntity cart = cartRepository.findById(cartItemRequestDto.getCartId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Cart not found"));

        ProductEntity product = productRepository.findById(cartItemRequestDto.getProductId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Product not found"));

        CartItemResponseDto cartItem;

        if (cartItemRepository.existsByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId())) {
            CartItemEntity existingCartItem = cartItemRepository.findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId())
                    .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Cart item not found"));
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequestDto.getQuantity());
            CartItemEntity updatedCartItem = cartItemRepository.save(existingCartItem);
            cartItem = mapper(updatedCartItem);
        } else {
            CartItemEntity newCartItem = new CartItemEntity();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(cartItemRequestDto.getQuantity());
            newCartItem.setAddedDate(LocalDateTime.now());
            CartItemEntity savedCartItem = cartItemRepository.save(newCartItem);
            cartItem = mapper(savedCartItem);
        }

        DiscountEntity discount = discountRepository.findCurrentDiscountForProduct(cartItem.getProduct().getProductId());
        if (discount != null) {
            DiscountDto discountDto = modelMapper.map(discount, DiscountDto.class);
            cartItem.getProduct().setDiscount(discountDto);
        }

        cartItem.getProduct().setCurrentPrice(getDiscountedPrice(cartItem.getProduct()));
        cartItem.getProduct().setRate(getRate(cartItem.getProduct()));

        return cartItem;
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
        return cartItems.stream()
                .map(cartItem->{
                    CartItemResponseDto cartItemResponseDto = mapper(cartItem);
                    DiscountEntity discount = discountRepository.findCurrentDiscountForProduct(cartItemResponseDto.getProduct().getProductId());
                    if (discount != null) {
                        DiscountDto discountDto = modelMapper.map(discount, DiscountDto.class);
                        cartItemResponseDto.getProduct().setDiscount(discountDto);
                    }
                    cartItemResponseDto.getProduct().setCurrentPrice(getDiscountedPrice(cartItemResponseDto.getProduct()));
                    cartItemResponseDto.getProduct().setRate(getRate(cartItemResponseDto.getProduct()));
                    return cartItemResponseDto;

        }).collect(Collectors.toList());
    }

    @Override
    public void deleteAllCartItems(Integer cartId) {
        cartItemRepository.deleteAllByCartCartId(cartId);
    }

    @Override
    public CartItemResponseDto updateQuantity(Integer cartItemId, CartItemRequestDto cartItemRequestDto) {
        CartItemEntity cartItemEntity = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Cart item not found")
                );
        cartItemEntity.setQuantity(cartItemRequestDto.getQuantity());
        CartItemEntity updatedCartItem = cartItemRepository.save(cartItemEntity);
        return mapper(updatedCartItem);
    }
}
