package com.dune.greensupermarketbackend.discount.service;

import com.dune.greensupermarketbackend.discount.DiscountEntity;
import com.dune.greensupermarketbackend.discount.dto.DiscountDto;

import java.util.List;

public interface DiscountService {
    DiscountDto createDiscount(DiscountDto discountDto);
    List<DiscountEntity> getAllDiscounts();
    DiscountDto getDiscountByDiscountId(Integer discountId);
    DiscountDto updateDiscount(Integer discountId,DiscountDto updatedDiscount);
    void deleteDiscount(Integer discountId);
    List<DiscountEntity> getDiscountsByProductId(Integer productId);
    DiscountDto getCurrentDiscountForProduct(Integer productId);
    List<DiscountEntity> getCurrentDiscounts();
}
