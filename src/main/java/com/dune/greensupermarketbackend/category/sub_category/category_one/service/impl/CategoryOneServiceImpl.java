package com.dune.greensupermarketbackend.category.sub_category.category_one.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.category.main_category.MainCategoryRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneDto;
import com.dune.greensupermarketbackend.category.sub_category.category_one.dto.CategoryOneResponseMessageDto;
import com.dune.greensupermarketbackend.category.sub_category.category_one.service.CategoryOneService;
import com.dune.greensupermarketbackend.exception.APIException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryOneServiceImpl implements CategoryOneService {

    private CategoryOneRepository categoryOneRepository;
    private MainCategoryRepository mainCategoryRepository;
    private ModelMapper modelMapper;

    private CategoryOneEntity checkCategoryOne(Integer subCatOneId) {
        return categoryOneRepository.findById(subCatOneId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
                        "Sub Category not found with ID: " + subCatOneId + "!"));
    }

    private void validateString(String value, String errorMessage) {
        if (value == null || value.isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    @Override
    public List<CategoryOneDto> getAllCategories() {
        List<CategoryOneEntity> categories = categoryOneRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category, CategoryOneDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryOneDto getCategoryById(Integer subCatOneId) {
        CategoryOneEntity category = checkCategoryOne(subCatOneId);
        return modelMapper.map(category, CategoryOneDto.class);
    }

    @Override
    public CategoryOneResponseMessageDto addCategory(CategoryOneDto categoryOneDto) {
        validateString(categoryOneDto.getSubCatOneName(), "Category name cannot be empty!");
        validateString(categoryOneDto.getSubCatOneDescription(), "Category description cannot be empty!");

        mainCategoryRepository.findById(categoryOneDto.getMainCategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Main category not found!"));

        CategoryOneEntity categoryOneEntity = modelMapper.map(categoryOneDto, CategoryOneEntity.class);
        categoryOneRepository.save(categoryOneEntity);
        return new CategoryOneResponseMessageDto(categoryOneDto.getSubCatOneName() + " added successful!");
    }

    @Override
    public CategoryOneResponseMessageDto updateCategory(Integer subCatOneId, CategoryOneDto updateCategory) {
        validateString(updateCategory.getSubCatOneName(), "Category name cannot be empty!");
        validateString(updateCategory.getSubCatOneDescription(), "Category description cannot be empty!");

        CategoryOneEntity category = checkCategoryOne(subCatOneId);
        category.setSubCatOneName(updateCategory.getSubCatOneName());
        category.setSubCatOneDescription(updateCategory.getSubCatOneDescription());
        categoryOneRepository.save(category);
        return new CategoryOneResponseMessageDto(updateCategory.getSubCatOneName() + " update successful!");
    }

    @Override
    public CategoryOneResponseMessageDto deleteBrand(Integer subCatOneId) {
        CategoryOneEntity category = checkCategoryOne(subCatOneId);
        categoryOneRepository.delete(category);
        return new CategoryOneResponseMessageDto(category.getSubCatOneName() + " deleted successfully!");
    }

}
