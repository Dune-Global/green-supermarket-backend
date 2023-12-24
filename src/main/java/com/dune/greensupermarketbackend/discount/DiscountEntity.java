package com.dune.greensupermarketbackend.discount;

import java.time.LocalDateTime;

import com.dune.greensupermarketbackend.product.ProductEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discount")
public class DiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "discount_description")
    private String description;

    @Column(name = "discount_start_date", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime startDate;

    @Column(name = "discount_end_date", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime endDate;

    @Column(name = "discount_rate", nullable = false)
    private Double rate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}