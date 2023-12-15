package com.dune.greensupermarketbackend.brand.service;

import java.util.List;

import com.dune.greensupermarketbackend.brand.dto.BrandDto;
import com.dune.greensupermarketbackend.brand.dto.BrandResponseMessageDto;

public interface BrandService {
   List<BrandDto> getAllBrands();

   BrandResponseMessageDto addBrand(BrandDto brandDto);

   BrandResponseMessageDto updateBrand(Integer brandId, BrandDto updatedBrand);

   BrandDto getBrandById(Integer brandId);

   BrandResponseMessageDto deleteBrand(Integer brandId);
}