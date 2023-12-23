package com.dune.greensupermarketbackend.discount;

import java.sql.Time;

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
    @Column(name = "discount_id", nullable = false)
    private Integer discountId;

    @Column(name = "discount_name", nullable = false)
    private String discountName;

    @Column(name = "discount_description", nullable = false)
    private String discountDescription;

    @Column(name = "discount_start_date", nullable = false)
    private Time discountStartDate;

    @Column(name = "discount_end_date", nullable = false)
    private Time discountEndDate;

    @Column(name = "discount_percentage", nullable = false)
    private Double discountPercentage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
