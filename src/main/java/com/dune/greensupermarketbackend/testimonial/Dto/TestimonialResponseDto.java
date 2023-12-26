package com.dune.greensupermarketbackend.testimonial.Dto;

import com.dune.greensupermarketbackend.customer.CustomerDto;
import com.dune.greensupermarketbackend.customer.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialResponseDto extends TestimonialDto{
    private CustomerDto reviwer;
}
