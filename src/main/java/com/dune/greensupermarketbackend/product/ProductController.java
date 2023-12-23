package com.dune.greensupermarketbackend.product;

import com.dune.greensupermarketbackend.ApiVersionConfig;
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

}
