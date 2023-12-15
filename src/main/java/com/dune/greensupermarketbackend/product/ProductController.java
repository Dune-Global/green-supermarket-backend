package com.dune.greensupermarketbackend.product;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.product.service.ProductService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @PostMapping("add-product")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto){
        ProductDto savedProduct = productService.addProduct(productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
}
