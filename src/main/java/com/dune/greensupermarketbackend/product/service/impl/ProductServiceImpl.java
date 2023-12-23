package com.dune.greensupermarketbackend.product.service.impl;

import com.dune.greensupermarketbackend.brand.BrandEntity;
import com.dune.greensupermarketbackend.brand.BrandRepository;
import com.dune.greensupermarketbackend.category.main_category.MainCategoryEntity;
import com.dune.greensupermarketbackend.category.main_category.MainCategoryRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoRepository;
import com.dune.greensupermarketbackend.exception.APIException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.product.ProductEntity;
import com.dune.greensupermarketbackend.product.ProductRepository;
import com.dune.greensupermarketbackend.product.dto.ProductDto;
import com.dune.greensupermarketbackend.product.service.ProductService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private BrandRepository brandRepository;
    private MainCategoryRepository mainCategoryRepository;
    private CategoryOneRepository categoryOneRepository;
    private CategoryTwoRepository categoryTwoRepository;
    private ModelMapper modelMapper;

    @Override
    public ProductDto addProduct(ProductDto productDto) {

        BrandEntity brand = brandRepository.findById(productDto.getBrandId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Brand ID not found!"));

        MainCategoryEntity mainCategory = mainCategoryRepository.findById(productDto.getMainCategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Main Category ID not found!"));

        CategoryOneEntity categoryOne = categoryOneRepository.findById(productDto.getL1CategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category One ID not found!"));

        CategoryTwoEntity categoryTwo = categoryTwoRepository.findById(productDto.getL2CategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category Two ID not found!"));

        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        productEntity.setBrand(brand);
        productEntity.setMainCategory(mainCategory);
        productEntity.setL1Category(categoryOne);
        productEntity.setL2Category(categoryTwo);

        System.out.println(productDto);
        System.out.println(productEntity);

        ProductEntity savedProduct = productRepository.save(productEntity);
        return modelMapper.map(savedProduct, ProductDto.class);
    }
}
