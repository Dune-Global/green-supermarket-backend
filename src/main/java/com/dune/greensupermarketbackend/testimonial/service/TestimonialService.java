package com.dune.greensupermarketbackend.testimonial.service;

import com.dune.greensupermarketbackend.testimonial.Dto.TestimonialRequestDto;
import com.dune.greensupermarketbackend.testimonial.Dto.TestimonialResponseDto;

import java.util.List;

public interface TestimonialService {
    List<TestimonialResponseDto> getAllTestimonials();
    TestimonialResponseDto addTestimonial(TestimonialRequestDto newTestimonial);
    List<TestimonialResponseDto> findAllByOrderByWrittenDateDesc();
}
