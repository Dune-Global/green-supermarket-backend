package com.dune.greensupermarketbackend.brand;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.brand.dto.BrandDto;
import com.dune.greensupermarketbackend.brand.dto.BrandResponseMessageDto;
import com.dune.greensupermarketbackend.brand.service.BrandService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/brands")
@AllArgsConstructor
public class BrandController {
    private BrandService brandService;

    // Get the list of brand
    @GetMapping("all-brands")
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<BrandDto> list = brandService.getAllBrands();
        return ResponseEntity.ok(list);
    }

    // Get brand by id
    @GetMapping("{brandId}")
    public ResponseEntity<BrandDto> getBrandById(@PathVariable("brandId") Integer brandId) {
        BrandDto brandDto = brandService.getBrandById(brandId);
        return new ResponseEntity<>(brandDto, HttpStatus.OK);
    }

    // Add brand
    @PostMapping("add-brand")
    public ResponseEntity<BrandResponseMessageDto> addBrand(@RequestBody BrandDto brandDto) {
        BrandResponseMessageDto brandResponseMessageDto = brandService.addBrand(brandDto);
        return new ResponseEntity<>(brandResponseMessageDto, HttpStatus.CREATED);
    }

    // Update brand
    @PutMapping("update-brand/{brandId}")
    public ResponseEntity<BrandResponseMessageDto> updateBrand(@PathVariable("brandId") Integer brandId,
            @RequestBody BrandDto updatedBrand) {
        BrandResponseMessageDto brandResponseMessageDto = brandService.updateBrand(brandId, updatedBrand);
        return new ResponseEntity<>(brandResponseMessageDto, HttpStatus.CREATED);
    }

    // Delete brand (WARNING: This endpoint will delete the product from the store)
    @DeleteMapping("delete-brand/{brandId}")
    public ResponseEntity<BrandResponseMessageDto> deleteBrand(@PathVariable("brandId") Integer brandId) {
        BrandResponseMessageDto brandResponseMessageDto = brandService.deleteBrand(brandId);
        return new ResponseEntity<>(brandResponseMessageDto, HttpStatus.OK);
    }
}
