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
import com.dune.greensupermarketbackend.product_rating.dto.RatingForProductDto;
import com.dune.greensupermarketbackend.product_rating.service.RatingService;
import jakarta.transaction.Transactional;
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
    private final RatingService ratingService;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository, DiscountRepository discountRepository, ModelMapper modelMapper, RatingService ratingService) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.modelMapper = modelMapper;
        this.ratingService = ratingService;
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

    public RatingForProductDto getRating(ProductDto productDto) {
        return ratingService.getAverageRatingByProductId(productDto.getProductId());
    }

    @Override
    public CartItemResponseDto addToCart(CartItemRequestDto cartItemRequestDto) {
        CartEntity cart = cartRepository.findById(cartItemRequestDto.getCartId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Cart not found"));

        ProductEntity product = productRepository.findById(cartItemRequestDto.getProductId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Product not found"));

        CartItemEntity existingCartItem = cartItemRepository.findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId())
                .orElse(null);

        int totalRequestedQuantity = cartItemRequestDto.getQuantity();
        if (existingCartItem != null) {
            totalRequestedQuantity += existingCartItem.getQuantity();
        }

        if (product.getStockAvailableUnits() < totalRequestedQuantity) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Only " + product.getStockAvailableUnits() + " units are available");
        }

        CartItemEntity cartItemEntity = existingCartItem != null ? existingCartItem : new CartItemEntity();

        cartItemEntity.setCart(cart);
        cartItemEntity.setProduct(product);
        cartItemEntity.setQuantity(totalRequestedQuantity);
        cartItemEntity.setAddedDate(LocalDateTime.now());

        cartItemRepository.save(cartItemEntity);

        CartItemResponseDto cartItem = mapper(cartItemEntity);

        DiscountEntity discount = discountRepository.findCurrentDiscountForProduct(cartItem.getProduct().getProductId());
        if (discount != null) {
            DiscountDto discountDto = modelMapper.map(discount, DiscountDto.class);
            cartItem.getProduct().setDiscount(discountDto);
        }

        cartItem.getProduct().setCurrentPrice(getDiscountedPrice(cartItem.getProduct()));
        cartItem.getProduct().setRating(getRating(cartItem.getProduct()));

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
                    cartItemResponseDto.getProduct().setRating(getRating(cartItemResponseDto.getProduct()));
                    return cartItemResponseDto;

        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteAllCartItems(Integer cartId) {
        cartItemRepository.deleteAllByCartCartId(cartId);
    }

    @Override
    public CartItemResponseDto updateQuantity(Integer cartItemId, CartItemRequestDto cartItemRequestDto) {
        CartItemEntity cartItemEntity = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Cart item not found"));

        ProductEntity product = cartItemEntity.getProduct();

        CartItemResponseDto cartItem = mapper(cartItemEntity);



        if (cartItemRequestDto.getQuantity() <= 0) {
            cartItemRepository.delete(cartItemEntity);
            throw  new APIException(HttpStatus.BAD_REQUEST,"Item removed from cart");
        } else if (product.getStockAvailableUnits() < cartItemRequestDto.getQuantity()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Only " + product.getStockAvailableUnits() + " units are available");
        } else {
            cartItemEntity.setQuantity(cartItemRequestDto.getQuantity());
            CartItemEntity updatedCartItem = cartItemRepository.save(cartItemEntity);

            CartItemResponseDto responseDto = mapper(updatedCartItem);

            DiscountEntity discount = discountRepository.findCurrentDiscountForProduct(cartItem.getProduct().getProductId());
            if (discount != null) {
                DiscountDto discountDto = modelMapper.map(discount, DiscountDto.class);
                responseDto.getProduct().setDiscount(discountDto);
            }

            responseDto.getProduct().setCurrentPrice(getDiscountedPrice(cartItem.getProduct()));
            responseDto.getProduct().setRating(getRating(cartItem.getProduct()));

            return responseDto;
        }
    }
}
