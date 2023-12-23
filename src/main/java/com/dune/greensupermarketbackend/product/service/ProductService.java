package com.dune.greensupermarketbackend.product.service;

import java.util.List;

import com.dune.greensupermarketbackend.product.dto.ProductDto;
import com.dune.greensupermarketbackend.product.dto.ProductResponseDto;
import com.dune.greensupermarketbackend.product.dto.ProductResponseMessageDto;

public interface ProductService {

    ProductResponseMessageDto addProduct(ProductDto productDto, String imgUrl);

    ProductResponseMessageDto updateProduct(Integer id, ProductDto productDto);

    ProductResponseMessageDto deleteProduct(Integer id);

    List<ProductResponseDto> getAllProductDetails();

    ProductResponseDto getProductDetails(Integer id);
}
