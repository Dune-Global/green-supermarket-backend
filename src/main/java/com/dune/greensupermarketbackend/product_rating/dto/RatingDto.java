package com.dune.greensupermarketbackend.product_rating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private Integer orderItemId;
    private Integer customerId;
    private Integer ratingId;
    private String review;
    private Integer rating;
    private String reviewDate;
}
