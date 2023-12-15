package com.dune.greensupermarketbackend.product.service.impl;

import com.dune.greensupermarketbackend.brand.BrandRepository;
import com.dune.greensupermarketbackend.exception.APIException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.product.ProductDto;
import com.dune.greensupermarketbackend.product.ProductEntity;
import com.dune.greensupermarketbackend.product.ProductRepository;
import com.dune.greensupermarketbackend.product.service.ProductService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;
    private BrandRepository brandRepository;
    private ModelMapper modelMapper;

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);

        brandRepository.findById(productDto.getBrandId()).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Brand ID not found!"));


        ProductEntity savedProduct = productRepository.save(productEntity);
        return modelMapper.map(savedProduct, ProductDto.class);
    }
}
