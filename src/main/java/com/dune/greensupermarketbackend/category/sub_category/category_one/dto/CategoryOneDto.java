package com.dune.greensupermarketbackend.category.sub_category.category_one.dto;

import java.util.List;

import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryOneDto {
    private Integer subCatOneId;
    private String subCatOneName;
    private String subCatOneDescription;
    private Integer mainCategoryId;
}