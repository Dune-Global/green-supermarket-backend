package com.dune.greensupermarketbackend.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private Double originalPrice;
    private Integer stockKeepingUnits;
    private Integer stockAvailableUnits;
    private Integer brandId;
    private Integer mainCategoryId;
    private Integer l1CategoryId;
    private Integer l2CategoryId;
}
