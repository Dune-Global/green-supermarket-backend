package com.dune.greensupermarketbackend.product;

import com.dune.greensupermarketbackend.brand.BrandEntity;

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
@Table(name = "product")
public class ProductEntity {
    @Id
    @Column(name = "product-id", nullable = false)
    private Integer productId;

    @Column(name = "product-name", nullable = false)
    private String productName;

    @Column(name = "product-description", nullable = false)
    private String productDescription;

    @Column(name = "original-price", nullable = false)
    private Double originalPrice;

    @Column(name = "stock-keeping-units", nullable = false)
    private Integer stockKeepingUnits;

    @Column(name = "stock-available-units", nullable = false)
    private Integer stockAvailableUnits;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "brand-id", nullable = false)
     private BrandEntity brand;
}
