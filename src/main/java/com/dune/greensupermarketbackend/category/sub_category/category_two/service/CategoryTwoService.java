package com.dune.greensupermarketbackend.category.sub_category.category_two.service;

import java.util.List;

import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoDto;
import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoResponseMessageDto;

public interface CategoryTwoService {

    List<CategoryTwoDto> getAllCategories();

    CategoryTwoDto getCategoryById(Integer subCatTwoId);

    CategoryTwoResponseMessageDto addCategory(CategoryTwoDto categoryTwoDto);

    CategoryTwoResponseMessageDto updateCategory(Integer subCatTwoId, CategoryTwoDto updateCategory);

    CategoryTwoResponseMessageDto deleteBrand(Integer subCatTwoId);
}
