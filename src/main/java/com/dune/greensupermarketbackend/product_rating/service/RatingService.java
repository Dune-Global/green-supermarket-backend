package com.dune.greensupermarketbackend.product_rating.service;

import com.dune.greensupermarketbackend.product_rating.dto.RatingDto;
import com.dune.greensupermarketbackend.product_rating.dto.RatingForProductDto;
import com.dune.greensupermarketbackend.product_rating.dto.RatingWithCustomerDto;

import java.util.List;

public interface RatingService {
    RatingDto addRating(RatingDto ratingDto);
    RatingDto updateRating(Integer id, RatingDto ratingDto);
    List<RatingWithCustomerDto> getRatingProductId(Integer productId);

    RatingForProductDto getAverageRatingByProductId(Integer productId);
}
