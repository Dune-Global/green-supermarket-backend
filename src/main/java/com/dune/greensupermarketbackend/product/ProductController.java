package com.dune.greensupermarketbackend.product;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.brand.dto.BrandDto;
import com.dune.greensupermarketbackend.product.dto.ProductDto;
import com.dune.greensupermarketbackend.product.dto.ProductResponseDto;
import com.dune.greensupermarketbackend.product.dto.ProductResponseMessageDto;
import com.dune.greensupermarketbackend.product.service.ProductService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @GetMapping("all-products")
    public ResponseEntity<List<ProductResponseDto>> getAllProductDetails() {
        List<ProductResponseDto> productDetails = productService.getAllProductDetails();
        return new ResponseEntity<>(productDetails, HttpStatus.OK);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<ProductResponseDto> getProductDetails(@PathVariable Integer id) {
        ProductResponseDto productDetails = productService.getProductDetails(id);
        return new ResponseEntity<>(productDetails, HttpStatus.OK);
    }

    @PostMapping("add-product")
    public ResponseEntity<ProductResponseMessageDto> addProduct(@RequestBody ProductDto productDto,
            @RequestHeader("imgUrl") String imgUrl) {
        ProductResponseMessageDto productResponseMessageDto = productService.addProduct(productDto, imgUrl);
        return new ResponseEntity<>(productResponseMessageDto, HttpStatus.CREATED);
    }

    @PatchMapping("update-product/{id}")
    public ResponseEntity<ProductResponseMessageDto> updateProduct(@PathVariable Integer id,
            @RequestBody ProductDto productDto) {
        ProductResponseMessageDto productResponseMessageDto = productService.updateProduct(id, productDto);
        return new ResponseEntity<>(productResponseMessageDto, HttpStatus.OK);
    }

    @DeleteMapping("delete-product/{id}")
    public ResponseEntity<ProductResponseMessageDto> deleteProduct(@PathVariable Integer id) {
        ProductResponseMessageDto productResponseMessageDto = productService.deleteProduct(id);
        return new ResponseEntity<>(productResponseMessageDto, HttpStatus.OK);
    }

    @GetMapping("main-category/{mainCatId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByMainCatId(@PathVariable Integer mainCatId) {
        List<ProductResponseDto> productDetails = productService.getProductsByMainCatId(mainCatId);
        return new ResponseEntity<>(productDetails, HttpStatus.OK);
    }

    @GetMapping("category-one/{catOneId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCatOneId(@PathVariable Integer catOneId) {
        List<ProductResponseDto> productDetails = productService.getProductsByCatOneId(catOneId);
        return new ResponseEntity<>(productDetails, HttpStatus.OK);
    }

    @GetMapping("category-two/{catTwoId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCatTwoId(@PathVariable Integer catTwoId) {
        List<ProductResponseDto> productDetails = productService.getProductsByCatTwoId(catTwoId);
        return new ResponseEntity<>(productDetails, HttpStatus.OK);
    }

    @GetMapping("brand/{brandId}")
    public ResponseEntity<List<ProductDto>> getProductsByBrand(@PathVariable Integer brandId) {
        List<ProductDto> productDetails = productService.getProductsByBrand(brandId);
        return new ResponseEntity<>(productDetails, HttpStatus.OK);
    }

    @GetMapping("all-products-without-subs")
    public ResponseEntity<List<ProductDto>> getProductsWithoutSubs() {
        List<ProductDto> productDetails = productService.getProductsWithoutSubs();
        return new ResponseEntity<>(productDetails, HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<ProductDto> findProductsByName(@RequestParam String name) {
        return productService.findProductsByName(name);
    }

    @GetMapping("brands/main/{mainCatId}")
    public ResponseEntity<List<BrandDto>> findBrandsOfMainCat(@PathVariable Integer mainCatId) {
        List<BrandDto> brands = productService.findBrandsOfMainCat(mainCatId);
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @GetMapping("brands/sub-one/{subCatOneId}")
    public ResponseEntity<List<BrandDto>> findBrandsOfSubCat(@PathVariable Integer subCatOneId) {
        List<BrandDto> brands = productService.findBrandsOfSubCat(subCatOneId);
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public List<ProductResponseDto> getFilteredProducts(@RequestParam(required = false) Integer mainCatId, @RequestParam(required = false) Integer subCatId, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice) {
        return productService.getFilteredProducts(mainCatId, subCatId, minPrice, maxPrice);
    }

}
