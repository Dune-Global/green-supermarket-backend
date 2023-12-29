package com.dune.greensupermarketbackend.order.order_item.service.impl;

import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemDto;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.discount.dto.DiscountDto;
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
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DiscountService discountService;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ModelMapper modelMapper, OrderRepository orderRepository, ProductRepository productRepository, DiscountService discountService) {
        this.orderItemRepository = orderItemRepository;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.discountService = discountService;
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

    private Double getCurrentDiscountForProduct(ProductEntity product){
        DiscountDto discountDto = discountService.getCurrentDiscountForProduct(product.getProductId());
        return discountDto.getDiscountRate();
    }


    private Double checkDiscountedPrice(Double price, Double discount){
        return price - (price * discount / 100);
    }

    @Override
    public OrderItemDto create(Integer orderId, CartItemResponseDto item) {
        OrderEntity order = checkOrder(orderId);
        ProductEntity product = checkProduct(item.getProduct().getProductId());

        Double originalPrice = product.getOriginalPrice();
        Double discount = getCurrentDiscountForProduct(product);
        Double discountPrice = checkDiscountedPrice(product.getOriginalPrice(),discount);
        Integer quantity = item.getQuantity();
        Double totalAmount = ((originalPrice-discountPrice) * quantity);

        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(originalPrice);
        orderItem.setDiscount(discountPrice*quantity);
        orderItem.setTotalAmount(totalAmount);

        orderItem = orderItemRepository.save(orderItem);

        ProductEntity purchasedProduct = productRepository.findById(product.getProductId())
                .orElseThrow(()->
                        new APIException(HttpStatus.NOT_FOUND,"Product not found with "+product.getProductId()+" id")
                );

        purchasedProduct.setStockAvailableUnits(purchasedProduct.getStockAvailableUnits()-quantity);
        productRepository.save(purchasedProduct);

        return modelMapper.map(orderItem,OrderItemDto.class);
    }

    @Override
    public OrderItemDto getByOrderItemId(Integer orderItemId) {
        OrderItemEntity orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(
                        ()-> new APIException(HttpStatus.NOT_FOUND,"Order Item not found with id "+orderItemId)
                );
        return modelMapper.map(orderItem,OrderItemDto.class);
    }

    @Override
    public List<OrderItemDto> getByOrderId(Integer orderId) {
        List<OrderItemEntity> orderItems = orderItemRepository.findAllByOrderOrderId(orderId);
        return orderItems.stream().map(
                (orderItem)->modelMapper
                        .map(orderItem,OrderItemDto.class)).collect(Collectors.toList()
        );
    }
}
