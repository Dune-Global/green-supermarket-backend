package com.dune.greensupermarketbackend.order.order_item.service.impl;

import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.discount.DiscountEntity;
import com.dune.greensupermarketbackend.discount.DiscountRepository;
import com.dune.greensupermarketbackend.discount.service.DiscountService;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.order.OrderEntity;
import com.dune.greensupermarketbackend.order.OrderRepository;
import com.dune.greensupermarketbackend.order.order_item.OrderItemDto;
import com.dune.greensupermarketbackend.order.order_item.OrderItemEntity;
import com.dune.greensupermarketbackend.order.order_item.OrderItemRepository;
import com.dune.greensupermarketbackend.order.order_item.service.OrderItemService;
import com.dune.greensupermarketbackend.product.ProductEntity;
import com.dune.greensupermarketbackend.product.ProductRepository;
import com.dune.greensupermarketbackend.product_rating.dto.RatingDto;
import com.dune.greensupermarketbackend.product_rating.RatingEntity;
import com.dune.greensupermarketbackend.product_rating.RatingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DiscountService discountService;
    private final DiscountRepository discountRepository;
    private final RatingRepository ratingRepository;
    private final ModelMapper modelMapper;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ModelMapper modelMapper, OrderRepository orderRepository, ProductRepository productRepository, DiscountService discountService, DiscountRepository discountRepository, RatingRepository ratingRepository, ModelMapper modelMapper1) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.discountService = discountService;
        this.discountRepository = discountRepository;
        this.ratingRepository = ratingRepository;
        this.modelMapper = modelMapper1;
    }


    private OrderEntity checkOrder(Integer orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(
                        ()-> new APIException(HttpStatus.NOT_FOUND,"Order not found with id "+orderId)
                );
    }

    private ProductEntity checkProduct(Integer productId){
        return productRepository.findById(productId)
                .orElseThrow(
                        ()-> new APIException(HttpStatus.NOT_FOUND,"Product not found with id "+productId)
                );
    }

    public Double getCurrentDiscountForProduct(ProductEntity product) {
        Double discount = 0.0;
        DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(product.getProductId());
        if (discountEntity != null) {
            discount = product.getOriginalPrice() * discountEntity.getRate() / 100;
        }else {
            discount = 0.0;
        }
        return discount;
    }

    private Double checkDiscountedPrice(Double price, Double discount){
        return price - discount;
    }

    private OrderItemDto mapper(OrderItemEntity orderItemEntity){
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setOrderItemId(orderItemEntity.getOrderItemId());
        orderItemDto.setOrderId(orderItemEntity.getOrder().getOrderId());
        orderItemDto.setProductId(orderItemEntity.getProduct().getProductId());
        orderItemDto.setProductName(orderItemEntity.getProduct().getProductName());
        orderItemDto.setProductImage(orderItemEntity.getProduct().getProductImage());
        orderItemDto.setQuantity(orderItemEntity.getQuantity());
        orderItemDto.setPrice(orderItemEntity.getPrice());
        orderItemDto.setDiscount(orderItemEntity.getDiscount());
        orderItemDto.setTotalAmount(orderItemEntity.getTotalAmount());

        RatingEntity ratingEntity = ratingRepository.findByOrderItemOrderItemId(orderItemEntity.getOrderItemId());
        if (ratingEntity != null) {
            RatingDto ratingDto = modelMapper.map(ratingEntity, RatingDto.class);
            orderItemDto.setRating(ratingDto);
        }

        return orderItemDto;
    }

    @Override
    public OrderItemDto create(Integer orderId, CartItemResponseDto item) {
        OrderEntity order = checkOrder(orderId);
        ProductEntity product = checkProduct(item.getProduct().getProductId());

        Double originalPrice = product.getOriginalPrice();
        Double discount = getCurrentDiscountForProduct(product);
        Double discountPrice = checkDiscountedPrice(product.getOriginalPrice(),discount);
        Integer quantity = item.getQuantity();
        Double totalAmount = (discountPrice * quantity);

        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(originalPrice);
        orderItem.setDiscount(discount*quantity);
        orderItem.setTotalAmount(totalAmount);

        orderItem = orderItemRepository.save(orderItem);

        ProductEntity purchasedProduct = productRepository.findById(product.getProductId())
                .orElseThrow(()->
                        new APIException(HttpStatus.NOT_FOUND,"Product not found with "+product.getProductId()+" id")
                );

        purchasedProduct.setStockAvailableUnits(purchasedProduct.getStockAvailableUnits()-quantity);
        productRepository.save(purchasedProduct);


        return mapper(orderItem);
    }

    @Override
    public OrderItemDto getByOrderItemId(Integer orderItemId) {
        OrderItemEntity orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(
                        ()-> new APIException(HttpStatus.NOT_FOUND,"Order Item not found with id "+orderItemId)
                );
        return mapper(orderItem);
    }

    @Override
    public List<OrderItemDto> getByOrderId(Integer orderId) {
        List<OrderItemEntity> orderItems = orderItemRepository.findAllByOrderOrderId(orderId);
        return orderItems.stream().map(
                this::mapper).collect(Collectors.toList()
        );
    }
}
