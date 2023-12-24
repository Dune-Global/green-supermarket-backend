package com.dune.greensupermarketbackend.product;

import com.dune.greensupermarketbackend.brand.BrandEntity;
import com.dune.greensupermarketbackend.category.main_category.MainCategoryEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoEntity;

import com.dune.greensupermarketbackend.discount.DiscountEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_image", nullable = false)
    private String productImage;

    @Column(name = "original_price", nullable = false)
    private Double originalPrice;

    @Column(name = "measuring_unit",nullable = false)
    private String measuringUnit;

    @Column(name = "stock_keeping_units", nullable = false)
    private Integer stockKeepingUnits;

    @Column(name = "stock_available_units", nullable = false)
    private Integer stockAvailableUnits;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "main_cat_id", nullable = false)
    private MainCategoryEntity mainCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "l1_cat_id", nullable = false)
    private CategoryOneEntity l1Category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "l2_cat_id")
    private CategoryTwoEntity l2Category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscountEntity> discounts;
}
