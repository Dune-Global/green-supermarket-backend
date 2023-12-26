package com.dune.greensupermarketbackend.testimonial;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<TestimonialEntity,Integer> {
    List<TestimonialEntity> findAllByOrderByWrittenDateDesc();
}
