package com.dune.greensupermarketbackend.testimonial.Dto;

import com.dune.greensupermarketbackend.customer.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialRequestDto extends TestimonialDto{
    private Integer customerId;
}
