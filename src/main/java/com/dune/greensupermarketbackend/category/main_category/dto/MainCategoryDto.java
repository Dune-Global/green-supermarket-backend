package com.dune.greensupermarketbackend.category.main_category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryDto {
    private Integer mainCategoryId;
    private String mainCategoryName;
    private String mainCategoryDesc;
}
