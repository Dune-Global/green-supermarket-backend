package com.dune.greensupermarketbackend.testimonial;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.testimonial.Dto.TestimonialRequestDto;
import com.dune.greensupermarketbackend.testimonial.Dto.TestimonialResponseDto;
import com.dune.greensupermarketbackend.testimonial.service.TestimonialService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/customers/testimonials")
@AllArgsConstructor
public class TestimonialController {
    private TestimonialService testimonialService;

    @PostMapping("/add-testimonial")
    public TestimonialResponseDto addTestimonial(@RequestBody TestimonialRequestDto newTestimonial){
        return testimonialService.addTestimonial(newTestimonial);
    }

    @GetMapping("/all-testimonials")
    public List<TestimonialResponseDto> getAllTestimonials(){
        return testimonialService.getAllTestimonials();
    }

    @GetMapping("/all-testimonials/ordered")
    public List<TestimonialResponseDto> getAllTestimonialsOrdered(){
        return testimonialService.findAllByOrderByWrittenDateDesc();
    }

}
