package com.dune.greensupermarketbackend.category.main_category.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.category.main_category.MainCategoryEntity;
import com.dune.greensupermarketbackend.category.main_category.MainCategoryRepository;
import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryDto;
import com.dune.greensupermarketbackend.category.main_category.dto.MainCategoryResponseMessageDto;
import com.dune.greensupermarketbackend.category.main_category.service.MainCategoryService;
import com.dune.greensupermarketbackend.exception.APIException;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MainCategoryServiceImpl implements MainCategoryService {

    private MainCategoryRepository mainCategoryRepository;
    private ModelMapper modelMapper;

    private MainCategoryEntity checkCategory(Integer mainCategoryId) {
        return mainCategoryRepository.findById(mainCategoryId)
                .orElseThrow(
                        () -> new APIException(HttpStatus.NOT_FOUND, "No category with ID: " + mainCategoryId + "!"));
    }

    @Override
    public List<MainCategoryDto> getAllCategories() {
        List<MainCategoryEntity> categories = mainCategoryRepository.findAll();
        return categories.stream().map(
                category -> modelMapper.map(category, MainCategoryDto.class)).collect(Collectors.toList());
    }

    @Override
    public MainCategoryDto getCategoryById(Integer mainCategoryId) {
        MainCategoryEntity category = checkCategory(mainCategoryId);
        return modelMapper.map(category, MainCategoryDto.class);
    }

    @Override
    public MainCategoryResponseMessageDto addCategory(MainCategoryDto mainCategoryDto,String imgUrl) {
        if ("".equals(mainCategoryDto.getMainCategoryName()) || mainCategoryDto.getMainCategoryName() == null) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Category cannot be empty!");
        }
        if (mainCategoryRepository.existsById(mainCategoryDto.getMainCategoryId())) {
            throw new APIException(HttpStatus.BAD_REQUEST,
                    "Main category already exists with id " + mainCategoryDto.getMainCategoryId() + "!");
        }

        MainCategoryEntity mainCategoryEntity = modelMapper.map(mainCategoryDto, MainCategoryEntity.class);
        mainCategoryEntity.setImgUrl(imgUrl);
        mainCategoryRepository.save(mainCategoryEntity);
        return new MainCategoryResponseMessageDto(mainCategoryDto.getMainCategoryName() + " added successful!");
    }

    @Override
    public MainCategoryResponseMessageDto updateCategory(Integer mainCategoryId, MainCategoryDto updateCategory,String imgUrl) {
        if ("".equals(updateCategory.getMainCategoryName()) || updateCategory.getMainCategoryName() == null) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Category cannot be empty!");
        }

        MainCategoryEntity category = checkCategory(mainCategoryId);
        category.setMainCategoryName(updateCategory.getMainCategoryName());
        category.setMainCategoryDesc(updateCategory.getMainCategoryDesc());
        category.setImgUrl(imgUrl);
        mainCategoryRepository.save(category);
        return new MainCategoryResponseMessageDto(updateCategory.getMainCategoryName() + " update successful!");
    }

    @Transactional
    @Override
    public MainCategoryResponseMessageDto deleteCategory(Integer mainCategoryId) {
        MainCategoryEntity category = checkCategory(mainCategoryId);
        mainCategoryRepository.delete(category);
        return new MainCategoryResponseMessageDto(category.getMainCategoryName() + " deleted successfully!");
    }

}
