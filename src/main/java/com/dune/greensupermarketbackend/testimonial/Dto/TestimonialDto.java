package com.dune.greensupermarketbackend.testimonial.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialDto {
    private String review;
    private Integer rating;
    private LocalDateTime writtenDate;
}
