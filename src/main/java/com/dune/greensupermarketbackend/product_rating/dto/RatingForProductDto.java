package com.dune.greensupermarketbackend.product_rating.dto;

import com.dune.greensupermarketbackend.config.CustomDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingForProductDto {
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double avgRating;
    private Integer noOfRatings;
}
