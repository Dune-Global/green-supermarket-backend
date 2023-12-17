package com.dune.greensupermarketbackend.category.sub_category.category_two.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTwoDto {
    private Integer subCatTwoId;
    private String subCatTwoName;
    private String subCatTwoDescription;
    private Integer subCatOneId;
}
