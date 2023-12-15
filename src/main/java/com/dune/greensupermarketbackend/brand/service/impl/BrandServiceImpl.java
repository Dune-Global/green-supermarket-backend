package com.dune.greensupermarketbackend.brand.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.brand.BrandEntity;
import com.dune.greensupermarketbackend.brand.BrandRepository;
import com.dune.greensupermarketbackend.brand.dto.BrandDto;
import com.dune.greensupermarketbackend.brand.dto.BrandResponseMessageDto;
import com.dune.greensupermarketbackend.brand.service.BrandService;
import com.dune.greensupermarketbackend.exception.APIException;

import lombok.AllArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {

    private BrandRepository brandRepository;
    private ModelMapper modelMapper;

    private BrandEntity checkBrand(Integer brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Brand not found with ID: " + brandId + "!"));
    }

    @Override
    public BrandResponseMessageDto addBrand(BrandDto brandDto) {
        if ("".equals(brandDto.getBrandName()) || brandDto.getBrandName() == null) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Brand cannot be empty!");
        }

        if (brandRepository.existsById(brandDto.getBrandId())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Brand ID already exists!");
        }

        BrandEntity brandEntity = modelMapper.map(brandDto, BrandEntity.class);
        brandRepository.save(brandEntity);
        return new BrandResponseMessageDto(brandDto.getBrandName() + " added successful!");
    }

    @Override
    public BrandResponseMessageDto updateBrand(Integer brandId, BrandDto brandDto) {

        if ("".equals(brandDto.getBrandName()) || brandDto.getBrandName() == null) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Brand cannot be empty!");
        }

        BrandEntity brand = checkBrand(brandId);
        brand.setBrandName(brandDto.getBrandName());
        brandRepository.save(brand);
        return new BrandResponseMessageDto(brandDto.getBrandName() + " update successful!");
    }

    @Override
    public List<BrandDto> getAllBrands() {
        List<BrandEntity> brands = brandRepository.findAll();
        return brands.stream().map((brand) -> modelMapper.map(brand, BrandDto.class)).collect(Collectors.toList());
    }

    @Override
    public BrandDto getBrandById(Integer brandId) {
        BrandEntity brand = checkBrand(brandId);
        return modelMapper.map(brand, BrandDto.class);
    }

    @Override
    public BrandResponseMessageDto deleteBrand(Integer brandId) {
        BrandEntity brand = checkBrand(brandId);
        brandRepository.delete(brand);
        return new BrandResponseMessageDto(brand.getBrandName() + " deleted successful!");
    }

}
