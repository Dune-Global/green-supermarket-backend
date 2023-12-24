package com.dune.greensupermarketbackend.category.sub_category.category_one.dto;

import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryOneWithSubsDto extends CategoryOneDto{
    private List<CategoryTwoDto> categoryTwos;
}
