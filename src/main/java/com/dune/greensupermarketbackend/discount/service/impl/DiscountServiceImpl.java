package com.dune.greensupermarketbackend.discount.service.impl;

import com.dune.greensupermarketbackend.discount.DiscountEntity;
import com.dune.greensupermarketbackend.discount.DiscountRepository;
import com.dune.greensupermarketbackend.discount.dto.DiscountDto;
import com.dune.greensupermarketbackend.discount.service.DiscountService;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.product.ProductEntity;
import com.dune.greensupermarketbackend.product.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private DiscountRepository discountRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    private ProductEntity checkProduct(Integer productId){
        return productRepository.findById(productId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Product Id "+ productId +" not found!")
                );
    }

    private void validateString(String value, String errorMessage) {
        if (value == null || value.isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    private ProductEntity validateDiscount(DiscountDto discountDto){
    if (discountDto.getProductId() == null) {
        throw new APIException(HttpStatus.BAD_REQUEST, "Product Id cannot be null");
    }
    ProductEntity product = checkProduct(discountDto.getProductId());

    if (discountDto.getDiscountRate() == null || discountDto.getDiscountRate() < 0.0 || discountDto.getDiscountRate() > 100.0) {
        throw new APIException(HttpStatus.BAD_REQUEST, "Discount rate cannot be null and must be between 0.0 and 100.0");
    }

    if (discountDto.getDiscountStartDate() == null || discountDto.getDiscountEndDate() == null) {
        throw new APIException(HttpStatus.BAD_REQUEST, "Discount start date and end date cannot be null");
    }

    if (discountDto.getDiscountStartDate().isAfter(discountDto.getDiscountEndDate())) {
        throw new APIException(HttpStatus.BAD_REQUEST, "Discount start date cannot be after end date");
    }
    return product;
    }

    public void isDiscountInTimeRange(DiscountDto discountDto){
        List<DiscountEntity> discounts = discountRepository
                .findDiscountsForProductInTimeRange(
                        discountDto.getProductId(),
                        discountDto.getDiscountStartDate(),
                        discountDto.getDiscountEndDate()
                );
        if(!discounts.isEmpty()){
            throw new APIException(HttpStatus.BAD_REQUEST, "Discount already exists for this time range");
        }
    }

    public void isDiscountInTimeRange(DiscountDto discountDto, Integer discountId){
    List<DiscountEntity> discounts = discountRepository
            .findDiscountsForProductInTimeRange(
                    discountDto.getProductId(),
                    discountDto.getDiscountStartDate(),
                    discountDto.getDiscountEndDate(),
                    discountId
            );
        if(!discounts.isEmpty()){
            throw new APIException(HttpStatus.BAD_REQUEST, "Discount already exists for this time range");
        }
    }

    @Override
    public DiscountDto createDiscount(DiscountDto discountDto) {
        ProductEntity product = validateDiscount(discountDto);
        isDiscountInTimeRange(discountDto);

        DiscountEntity discount = new DiscountEntity();

        discount.setProduct(product);
        discount.setDescription(discountDto.getDiscountDescription());
        discount.setRate(discountDto.getDiscountRate());
        discount.setStartDate(discountDto.getDiscountStartDate());
        discount.setEndDate(discountDto.getDiscountEndDate());
        discountRepository.save(discount);

        return modelMapper.map(discount,DiscountDto.class);
    }

    @Override
    public List<DiscountDto> getAllDiscounts() {
        List<DiscountEntity> discountEntities = discountRepository.findAll();
        return discountEntities.stream()
                .map(discountEntity -> modelMapper.map(discountEntity,DiscountDto.class)).toList();
    }

    @Override
    public DiscountDto getDiscountByDiscountId(Integer discountId) {
        return modelMapper.map(discountRepository.findById(discountId)
                .orElseThrow(
                        ()->new APIException(HttpStatus.NOT_FOUND,"Discount Id "+ discountId +" not found!")
                ),DiscountDto.class);
    }

    @Override
    public DiscountDto updateDiscount(Integer discountId, DiscountDto updatedDiscount) {
        DiscountEntity discount = discountRepository.findById(discountId)
                .orElseThrow(
                        ()->new APIException(HttpStatus.NOT_FOUND,"Discount Id "+ discountId +" not found!")
                );

        ProductEntity product = validateDiscount(updatedDiscount);
        isDiscountInTimeRange(updatedDiscount, discountId);

        discount.setProduct(product);
        discount.setDescription(updatedDiscount.getDiscountDescription());
        discount.setRate(updatedDiscount.getDiscountRate());
        discount.setStartDate(updatedDiscount.getDiscountStartDate());
        discount.setEndDate(updatedDiscount.getDiscountEndDate());
        discountRepository.save(discount);

        return modelMapper.map(discount,DiscountDto.class);
    }

    @Override
    public void deleteDiscount(Integer discountId) {
        discountRepository.findById(discountId)
                .orElseThrow(
                        ()->new APIException(HttpStatus.NOT_FOUND,"Discount Id "+ discountId +" not found!")
                );
        discountRepository.deleteById(discountId);
    }

    @Override
    public List<DiscountDto> getDiscountsByProductId(Integer productId) {
        checkProduct(productId);
        List<DiscountEntity> discount = discountRepository.findByProductProductId(productId);
        return discount.stream()
                .map(discountEntity -> modelMapper.map(discountEntity,DiscountDto.class)).toList();
    }

    @Override
    public DiscountDto getCurrentDiscountForProduct(Integer productId) {
        checkProduct(productId);
        DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(productId);
        if (discountEntity != null) {
            return modelMapper.map(discountEntity, DiscountDto.class);
        } else {
            return null;
        }
    }

    @Override
    public List<DiscountDto> getCurrentDiscounts() {
        List<DiscountEntity> discounts = discountRepository.findCurrentDiscounts();
        return discounts.stream()
                .map(discountEntity -> modelMapper.map(discountEntity,DiscountDto.class)).toList();
    }
}
