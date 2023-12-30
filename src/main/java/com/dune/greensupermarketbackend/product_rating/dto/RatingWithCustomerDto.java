package com.dune.greensupermarketbackend.product_rating.dto;

import com.dune.greensupermarketbackend.customer.CustomerDto;
import com.dune.greensupermarketbackend.product_rating.dto.RatingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingWithCustomerDto {
    private CustomerDto customer;
    private RatingDto rating;
}
