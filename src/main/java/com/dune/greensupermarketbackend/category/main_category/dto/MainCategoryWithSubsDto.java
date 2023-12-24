package com.dune.greensupermarketbackend.category.main_category.dto;

import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneDto;
import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneWithSubsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryWithSubsDto {
    private Integer mainCategoryId;
    private String mainCategoryName;
    private String mainCategoryDesc;
    private String imgUrl;
    private List<CategoryOneWithSubsDto> categoryOnes;
}
