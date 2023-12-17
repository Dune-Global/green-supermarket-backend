package com.dune.greensupermarketbackend.category.sub_category.category_two.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoDto;
import com.dune.greensupermarketbackend.category.sub_category.category_two.dto.CategoryTwoResponseMessageDto;
import com.dune.greensupermarketbackend.category.sub_category.category_two.service.CategoryTwoService;
import com.dune.greensupermarketbackend.exception.APIException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryServiceTwoImpl implements CategoryTwoService {
    private CategoryTwoRepository categoryTwoRepository;
    private CategoryOneRepository categoryOneRepository;
    private ModelMapper modelMapper;

    private CategoryTwoEntity checkCategoryTwo(Integer subCatTwoId) {
        return categoryTwoRepository.findById(subCatTwoId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
                        "Sub Category not found with ID: " + subCatTwoId + "!"));
    }

    private void validateString(String value, String errorMessage) {
        if (value == null || value.isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    @Override
    public List<CategoryTwoDto> getAllCategories() {
        List<CategoryTwoEntity> categories = categoryTwoRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category, CategoryTwoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryTwoDto getCategoryById(Integer subCatTwoId) {
        CategoryTwoEntity category = checkCategoryTwo(subCatTwoId);
        return modelMapper.map(category, CategoryTwoDto.class);
    }

    @Override
    public CategoryTwoResponseMessageDto addCategory(CategoryTwoDto categoryTwoDto) {
        validateString(categoryTwoDto.getSubCatTwoName(), "Category name cannot be empty!");
        validateString(categoryTwoDto.getSubCatTwoDescription(), "Category description cannot be empty!");

        categoryOneRepository.findById(categoryTwoDto.getSubCatOneId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Main category not found!"));

        CategoryTwoEntity categoryTwoEntity = modelMapper.map(categoryTwoDto, CategoryTwoEntity.class);
        categoryTwoRepository.save(categoryTwoEntity);
        return new CategoryTwoResponseMessageDto(categoryTwoDto.getSubCatTwoName() + " added successful!");
    }

    @Override
    public CategoryTwoResponseMessageDto updateCategory(Integer subCatTwoId, CategoryTwoDto updateCategory) {
        validateString(updateCategory.getSubCatTwoName(), "Category name cannot be empty!");
        validateString(updateCategory.getSubCatTwoDescription(), "Category description cannot be empty!");

        CategoryTwoEntity category = checkCategoryTwo(subCatTwoId);
        category.setSubCatTwoName(updateCategory.getSubCatTwoName());
        category.setSubCatTwoDescription(updateCategory.getSubCatTwoDescription());
        categoryTwoRepository.save(category);
        return new CategoryTwoResponseMessageDto(updateCategory.getSubCatTwoName() + " update successful!");
    }

    @Override
    public CategoryTwoResponseMessageDto deleteBrand(Integer subCatTwoId) {
        CategoryTwoEntity category = checkCategoryTwo(subCatTwoId);
        categoryTwoRepository.delete(category);
        return new CategoryTwoResponseMessageDto(category.getSubCatTwoName() + " deleted successfully!");
    }
}
