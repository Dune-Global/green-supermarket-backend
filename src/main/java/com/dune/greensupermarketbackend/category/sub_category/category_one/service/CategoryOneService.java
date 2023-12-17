package com.dune.greensupermarketbackend.category.sub_category.category_one.service;

import java.util.List;

import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneDto;
import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneResponseMessageDto;

public interface CategoryOneService {

    List<CategoryOneDto> getAllCategories();

    CategoryOneDto getCategoryById(Integer subCatOneId);

    CategoryOneResponseMessageDto addCategory(CategoryOneDto categoryOneDto);

    CategoryOneResponseMessageDto updateCategory(Integer subCatOneId, CategoryOneDto updateCategory);

    CategoryOneResponseMessageDto deleteBrand(Integer subCatOneId);

    
}
