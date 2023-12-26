package com.dune.greensupermarketbackend.testimonial;

import com.dune.greensupermarketbackend.customer.CustomerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customer_testimonial")
public class TestimonialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "review", nullable = false)
    private String review;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "written-date", nullable = false)
    private LocalDateTime writtenDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity reviwer;
}
