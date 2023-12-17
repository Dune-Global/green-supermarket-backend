package com.dune.greensupermarketbackend.category.main_category.service;

import java.util.List;

import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryDto;
import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryResponseMessageDto;

public interface MainCategoryService {

    public List<MainCategoryDto> getAllCategories();

    public MainCategoryDto getCategoryById(Integer mainCategoryId);

    public MainCategoryResponseMessageDto addCategory(MainCategoryDto mainCategoryDto);

    public MainCategoryResponseMessageDto updateCategory(Integer mainCategoryId, MainCategoryDto updateCategory);

    public MainCategoryResponseMessageDto deleteBrand(Integer mainCategoryId);

}
