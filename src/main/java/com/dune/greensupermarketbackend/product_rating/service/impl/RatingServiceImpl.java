package com.dune.greensupermarketbackend.product_rating.service.impl;

import com.dune.greensupermarketbackend.customer.CustomerDto;
import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.CustomerRepository;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.order.OrderRepository;
import com.dune.greensupermarketbackend.order.order_item.OrderItemEntity;
import com.dune.greensupermarketbackend.order.order_item.OrderItemRepository;
import com.dune.greensupermarketbackend.product.ProductEntity;
import com.dune.greensupermarketbackend.product.ProductRepository;
import com.dune.greensupermarketbackend.product_rating.*;
import com.dune.greensupermarketbackend.product_rating.dto.RatingDto;
import com.dune.greensupermarketbackend.product_rating.dto.RatingForProductDto;
import com.dune.greensupermarketbackend.product_rating.dto.RatingWithCustomerDto;
import com.dune.greensupermarketbackend.product_rating.service.RatingService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public RatingServiceImpl(RatingRepository ratingRepository, CustomerRepository customerRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository, ModelMapper modelMapper, ProductRepository productRepository1, OrderRepository orderRepository) {
        this.ratingRepository = ratingRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository1;

        // Explicitly define the mapping
        modelMapper.addMappings(new PropertyMap<RatingEntity, RatingDto>() {
            @Override
            protected void configure() {
                map().setOrderItemId(source.getOrderItem().getOrderItemId());
            }
        });
    }

    private CustomerEntity checkCustomer(Integer customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(  () -> new APIException(HttpStatus.NOT_FOUND,"Customer not found with Id: " + customerId)
                );
    }

    private OrderItemEntity checkOrderItem(Integer orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(  () -> new APIException(HttpStatus.NOT_FOUND,"Order Item not found with Id: " + orderItemId)
                );
    }

    private ProductEntity checkProduct(Integer productId){
        return productRepository.findById(productId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Product not found with Id: " + productId)
                );
    }

    private void validateRating(RatingDto ratingDto) {
        if (ratingDto.getRating() < 1 || ratingDto.getRating() > 5) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }
    }

    @Override
    public RatingDto addRating(RatingDto ratingDto) {

        OrderItemEntity orderItem = orderItemRepository.findById(ratingDto.getOrderItemId())
                .orElseThrow(  () -> new APIException(HttpStatus.NOT_FOUND,"Order Item not found with Id: " + ratingDto.getOrderItemId())
                );

        if(orderItem.getOrder().getOrderStatus().equals("Delivered")){
            throw new APIException(HttpStatus.BAD_REQUEST,"You can't rate this product because it is not delivered yet.");
        }

        validateRating(ratingDto);

        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setRating(ratingDto.getRating());
        ratingEntity.setReview(ratingDto.getReview());
        ratingEntity.setCustomer(checkCustomer(orderItem.getOrder().getCustomer().getCart().getCustomer().getId()));
        ratingEntity.setOrderItem(checkOrderItem(ratingDto.getOrderItemId()));
        ratingEntity.setReviewDate(LocalDateTime.now());

        return modelMapper.map(ratingRepository.save(ratingEntity), RatingDto.class);
    }

    @Override
    public RatingDto updateRating(Integer id, RatingDto ratingDto) {
        RatingEntity rating = ratingRepository.findByOrderItemOrderItemId(id);

        validateRating(ratingDto);

        rating.setReviewDate(LocalDateTime.now());
        rating.setRating(ratingDto.getRating());
        rating.setReview(ratingDto.getReview());
        return modelMapper.map(ratingRepository.save(rating),RatingDto.class);
    }

    @Override
    public List<RatingWithCustomerDto> getRatingProductId(Integer productId) {
        checkProduct(productId);

        List<RatingEntity> ratingEntities = ratingRepository.findRatingsByProductId(productId);
        List<RatingWithCustomerDto> ratingWithCustomerDtos = new ArrayList<>();

        for (RatingEntity ratingEntity : ratingEntities) {
            RatingDto ratingDto = modelMapper.map(ratingEntity, RatingDto.class);
            CustomerDto customerDto = modelMapper.map(ratingEntity.getCustomer(), CustomerDto.class);

            RatingWithCustomerDto ratingWithCustomerDto = new RatingWithCustomerDto();
            ratingWithCustomerDto.setCustomer(customerDto);
            ratingWithCustomerDto.setRating(ratingDto);

            ratingWithCustomerDtos.add(ratingWithCustomerDto);
        }

        return ratingWithCustomerDtos;
    }

    @Override
    public RatingForProductDto getAverageRatingByProductId(Integer productId) {
        checkProduct(productId);
        Double avgRating = ratingRepository.findAverageRatingByProductId(productId);
        if(avgRating == null){
            avgRating = 0.0;
        }
        Long noOfRatings = ratingRepository.countRatingsByProductId(productId);

        return new RatingForProductDto(avgRating, noOfRatings.intValue());
    }
}
