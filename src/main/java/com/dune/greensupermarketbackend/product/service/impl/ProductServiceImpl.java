package com.dune.greensupermarketbackend.product.service.impl;

import com.dune.greensupermarketbackend.brand.BrandEntity;
import com.dune.greensupermarketbackend.brand.BrandRepository;
import com.dune.greensupermarketbackend.category.main_category.MainCategoryEntity;
import com.dune.greensupermarketbackend.category.main_category.MainCategoryRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoRepository;
import com.dune.greensupermarketbackend.discount.DiscountRepository;
import com.dune.greensupermarketbackend.discount.dto.DiscountDto;
import com.dune.greensupermarketbackend.exception.APIException;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dune.greensupermarketbackend.product.ProductEntity;
import com.dune.greensupermarketbackend.product.ProductRepository;
import com.dune.greensupermarketbackend.product.dto.ProductDto;
import com.dune.greensupermarketbackend.product.dto.ProductResponseDto;
import com.dune.greensupermarketbackend.product.dto.ProductResponseMessageDto;
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
    private DiscountRepository discountRepository;
    private ModelMapper modelMapper;

    @Override
    public ProductResponseMessageDto addProduct(ProductDto productDto, String imgUrl) {

        if (productDto.getProductId() == null || productDto.getProductName().isEmpty()
                || productDto.getProductDescription().isEmpty() || imgUrl.isEmpty()
                || productDto.getOriginalPrice() == null || productDto.getStockKeepingUnits() == null
                || productDto.getStockAvailableUnits() == null || productDto.getBrandId() == null
                || productDto.getMainCategoryId() == null || productDto.getL1CategoryId() == null) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Fields cannot be null!");
        }

        if (productRepository.findById(productDto.getProductId()).isPresent()) {
            throw new APIException(HttpStatus.CONFLICT, "Product is already added!");
        }

        BrandEntity brand = brandRepository.findById(productDto.getBrandId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Brand ID not found!"));

        MainCategoryEntity mainCategory = mainCategoryRepository.findById(productDto.getMainCategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Main Category ID not found!"));

        CategoryOneEntity categoryOne = categoryOneRepository.findById(productDto.getL1CategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category One ID not found!"));

        CategoryTwoEntity categoryTwo = null;
        if (productDto.getL2CategoryId() != null) {
            categoryTwo = categoryTwoRepository.findById(productDto.getL2CategoryId())
                    .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category Two ID not found!"));
        }

        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        productEntity.setProductImage(imgUrl);
        productEntity.setBrand(brand);
        productEntity.setMainCategory(mainCategory);
        productEntity.setL1Category(categoryOne);
        productEntity.setL2Category(categoryTwo);

        ProductEntity savedProduct = productRepository.save(productEntity);
        return new ProductResponseMessageDto(savedProduct.getProductName() + " added successfully!");
    }

    @Override
    public ProductResponseMessageDto updateProduct(Integer id, ProductDto productDto) {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Product not found!"));

        BrandEntity brand = brandRepository.findById(productDto.getBrandId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Brand ID not found!"));

        MainCategoryEntity mainCategory = mainCategoryRepository.findById(productDto.getMainCategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Main Category ID not found!"));

        CategoryOneEntity categoryOne = categoryOneRepository.findById(productDto.getL1CategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category One ID not found!"));

        CategoryTwoEntity categoryTwo = null;
        if (productDto.getL2CategoryId() != null) {
            categoryTwo = categoryTwoRepository.findById(productDto.getL2CategoryId())
                    .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category Two ID not found!"));
        }

        modelMapper.map(productDto, existingProduct);
        existingProduct.setBrand(brand);
        existingProduct.setMainCategory(mainCategory);
        existingProduct.setL1Category(categoryOne);
        existingProduct.setL2Category(categoryTwo);

        ProductEntity updatedProduct = productRepository.save(existingProduct);

        return new ProductResponseMessageDto(updatedProduct.getProductName() + " updated successfully!");
    }

    @Override
    public ProductResponseMessageDto deleteProduct(Integer id) {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Product not found!"));

        productRepository.delete(existingProduct);

        return new ProductResponseMessageDto(existingProduct.getProductName() + " deleted successfully!");
    }

    @Override
    public List<ProductResponseDto> getAllProductDetails() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream().map(productEntity -> {
            ProductResponseDto productResponseDto = modelMapper.map(productEntity, ProductResponseDto.class);
            productResponseDto.setBrandName(productEntity.getBrand().getBrandName());
            productResponseDto.setMainCategoryName(productEntity.getMainCategory().getMainCategoryName());
            productResponseDto.setL1CategoryName(productEntity.getL1Category().getSubCatOneName());
            if (productEntity.getL2Category() != null) {
                productResponseDto.setL2CategoryName(productEntity.getL2Category().getSubCatTwoName());
            }
            return productResponseDto;
        }).collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductDetails(Integer id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Product not found!"));

        ProductResponseDto productResponseDto = modelMapper.map(productEntity, ProductResponseDto.class);
        productResponseDto.setBrandName(productEntity.getBrand().getBrandName());
        productResponseDto.setMainCategoryName(productEntity.getMainCategory().getMainCategoryName());
        productResponseDto.setL1CategoryName(productEntity.getL1Category().getSubCatOneName());
        if (productEntity.getL2Category() != null) {
            productResponseDto.setL2CategoryName(productEntity.getL2Category().getSubCatTwoName());
        }

        return productResponseDto;
    }

    @Override
    public List<ProductResponseDto> getProductsByMainCatId(Integer mainCatId) {
        mainCategoryRepository.findById(mainCatId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Main category not found with id: "+mainCatId));
        List<ProductEntity> products = productRepository.findByMainCategoryMainCategoryId(mainCatId);
        return products.stream()
            .map(product -> {
                ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
                productResponseDto.setDiscount(
                        modelMapper.map(discountRepository.findCurrentDiscountForProduct(product.getProductId()), DiscountDto.class)
            );
            return productResponseDto;
        })
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getProductsByCatOneId(Integer catOneId) {
        categoryOneRepository.findById(catOneId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Category one not found with id: "+catOneId));
        List<ProductEntity> products = productRepository.findByL1CategorySubCatOneId(catOneId);
        return products.stream()
                .map(product -> {
                    ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
                    productResponseDto.setDiscount(
                            modelMapper.map(discountRepository.findCurrentDiscountForProduct(product.getProductId()), DiscountDto.class)
                    );
                    return productResponseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getProductsByCatTwoId(Integer catTwoId) {
        categoryTwoRepository.findById(catTwoId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Category two not found with id: "+catTwoId));
        List<ProductEntity> products = productRepository.findByL2CategorySubCatTwoId(catTwoId);
        return products.stream()
                .map(product -> {
                    ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
                    productResponseDto.setDiscount(
                            modelMapper.map(discountRepository.findCurrentDiscountForProduct(product.getProductId()), DiscountDto.class)
                    );
                    return productResponseDto;
                })
                .collect(Collectors.toList());
    }
}
