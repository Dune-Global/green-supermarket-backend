package com.dune.greensupermarketbackend.testimonial.service.impl;

import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.CustomerRepository;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.order.OrderEntity;
import com.dune.greensupermarketbackend.order.OrderRepository;
import com.dune.greensupermarketbackend.testimonial.Dto.TestimonialRequestDto;
import com.dune.greensupermarketbackend.testimonial.Dto.TestimonialResponseDto;
import com.dune.greensupermarketbackend.testimonial.TestimonialEntity;
import com.dune.greensupermarketbackend.testimonial.TestimonialRepository;
import com.dune.greensupermarketbackend.testimonial.service.TestimonialService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TestimonialServiceImpl implements TestimonialService {

    private TestimonialRepository testimonialRepository;
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;
    private ModelMapper modelMapper;

    public void validateTestimonialRequest(TestimonialRequestDto testimonialRequest) {
    if (testimonialRequest.getRating() == null) {
        throw new APIException(HttpStatus.BAD_REQUEST,"Rating cannot be null");
    }

    if (testimonialRequest.getRating() < 1 || testimonialRequest.getRating() > 5) {
        throw new APIException(HttpStatus.BAD_REQUEST,"Rating should be an integer from 1 to 5");
    }

    if (testimonialRequest.getReview() == null || testimonialRequest.getReview().isEmpty()) {
        throw new APIException(HttpStatus.BAD_REQUEST,"Review cannot be empty");
    }
}

    @Override
    public List<TestimonialResponseDto> getAllTestimonials() {
        List<TestimonialEntity> testimonials = testimonialRepository.findAll();
        return  testimonials.stream()
                .map(testimonial->modelMapper.map(testimonial, TestimonialResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TestimonialResponseDto addTestimonial(TestimonialRequestDto newTestimonial){
        validateTestimonialRequest(newTestimonial);

        CustomerEntity customer = customerRepository.findById(newTestimonial.getCustomerId())
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Customer not found"));

        List<OrderEntity> orders = orderRepository.findByCustomerIdAndOrderStatus(customer.getId(), "Delivered");
        if (orders.isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "You need to have at least one delivered order to write a testimonial");
        }

        TestimonialEntity testimonialEntity = modelMapper.map(newTestimonial,TestimonialEntity.class);
        testimonialEntity.setId(null);

        testimonialEntity.setReviwer(customer);
        testimonialEntity.setWrittenDate(java.time.LocalDateTime.now());

        TestimonialEntity addedTestimonial = testimonialRepository.save(testimonialEntity);

        return modelMapper.map(addedTestimonial, TestimonialResponseDto.class);
    }

    @Override
    public List<TestimonialResponseDto> findAllByOrderByWrittenDateDesc() {
        List<TestimonialEntity> testimonials = testimonialRepository.findAllByOrderByWrittenDateDesc();
        return testimonials.stream()
                .map(testimonial -> modelMapper.map(testimonial, TestimonialResponseDto.class))
                .collect(Collectors.toList()
                );
    }
}
