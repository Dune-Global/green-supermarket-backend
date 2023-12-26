package com.dune.greensupermarketbackend.product.dto;

import com.dune.greensupermarketbackend.brand.dto.BrandDto;
import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryDto;
import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneDto;
import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoDto;

import com.dune.greensupermarketbackend.config.CustomDoubleSerializer;
import com.dune.greensupermarketbackend.discount.dto.DiscountDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Integer productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private String measuringUnit;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double originalPrice;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double currentPrice;
    private Integer stockKeepingUnits;
    private Integer stockAvailableUnits;
    private BrandDto brand;
    private MainCategoryDto mainCategory;
    private CategoryOneDto l1Category;
    private CategoryTwoDto l2Category;
    private String brandName;
    private String mainCategoryName;
    private String l1CategoryName;
    private String l2CategoryName;
    private DiscountDto discount;
    private Double rate;
}
