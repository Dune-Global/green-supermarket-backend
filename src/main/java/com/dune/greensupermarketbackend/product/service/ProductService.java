package com.dune.greensupermarketbackend.product.service;

import java.util.List;

import com.dune.greensupermarketbackend.brand.dto.BrandDto;
import com.dune.greensupermarketbackend.product.ProductEntity;
import com.dune.greensupermarketbackend.product.dto.ProductDto;
import com.dune.greensupermarketbackend.product.dto.ProductResponseDto;
import com.dune.greensupermarketbackend.product.dto.ProductResponseMessageDto;

public interface ProductService {

    ProductResponseMessageDto addProduct(ProductDto productDto, String imgUrl);

    ProductResponseMessageDto updateProduct(Integer id, ProductDto productDto);

    ProductResponseMessageDto deleteProduct(Integer id);

    List<ProductResponseDto> getAllProductDetails();

    ProductResponseDto getProductDetails(Integer id);
    List<ProductResponseDto> getProductsByMainCatId(Integer mainCatId);
    List<ProductResponseDto> getProductsByCatOneId(Integer catOneId);
    List<ProductResponseDto> getProductsByCatTwoId(Integer catTwoId);
    List<ProductDto> getProductsByBrand(Integer brandId);
    List<ProductDto> getProductsWithoutSubs();
    List<ProductDto> findProductsByName(String productName);
    List<BrandDto> findBrandsOfMainCat(Integer mainCatId);
    List<BrandDto> findBrandsOfSubCat(Integer subCatOneId);
}
