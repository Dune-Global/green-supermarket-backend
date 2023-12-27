package com.dune.greensupermarketbackend.product.service.impl;

import com.dune.greensupermarketbackend.brand.BrandEntity;
import com.dune.greensupermarketbackend.brand.BrandRepository;
import com.dune.greensupermarketbackend.category.main_category.MainCategoryEntity;
import com.dune.greensupermarketbackend.category.main_category.MainCategoryRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneRepository;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoRepository;
import com.dune.greensupermarketbackend.discount.DiscountEntity;
import com.dune.greensupermarketbackend.discount.DiscountRepository;
import com.dune.greensupermarketbackend.discount.dto.DiscountDto;
import com.dune.greensupermarketbackend.exception.APIException;

import java.util.List;
import java.util.Objects;
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

    public void checkCategories(ProductDto productDto){
        Integer mainCatId = productDto.getMainCategoryId();
        Integer catOneId = productDto.getL1CategoryId();
        Integer catTwoId = productDto.getL2CategoryId();
        CategoryOneEntity categoryOne = categoryOneRepository.findById(catOneId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Category one not found with id: "+catOneId));

        CategoryTwoEntity categoryTwo = categoryTwoRepository.findById(catTwoId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Category two not found with id: "+catTwoId));

        if(!Objects.equals(categoryOne.getMainCategory().getMainCategoryId(), mainCatId)){
            throw new APIException(HttpStatus.BAD_REQUEST,"Category one does not belong to main category");
        }
        if(!Objects.equals(categoryTwo.getSubCatTwoId(), catOneId)){
            throw new APIException(HttpStatus.BAD_REQUEST,"Category two does not belong to category one");
        }
    }

    public Double getDiscountedPrice(ProductEntity productEntity) {
        Double discountedPrice = null;
        DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(productEntity.getProductId());
        if (discountEntity != null) {
            discountedPrice =  productEntity.getOriginalPrice() - (productEntity.getOriginalPrice() * discountEntity.getRate() / 100);
        }else {
            discountedPrice = productEntity.getOriginalPrice();
        }
        return discountedPrice;
    }

    public Double getRate(ProductEntity productEntity) {
        Double rate = 5.0;
        return rate;
    }


    @Override
    public ProductResponseMessageDto addProduct(ProductDto productDto, String imgUrl) {

    if (productDto.getProductId() == null || productDto.getProductName().isEmpty()
            || productDto.getOriginalPrice() == null || productDto.getStockKeepingUnits() == null
            || productDto.getStockAvailableUnits() == null || productDto.getBrandId() == null
            || productDto.getMainCategoryId() == null || productDto.getL1CategoryId() == null
            || productDto.getMeasuringUnit() == null) {
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

    if (!categoryOne.getMainCategory().equals(mainCategory)) {
        throw new APIException(HttpStatus.BAD_REQUEST, "Category One does not belong to the Main Category");
    }

    CategoryTwoEntity categoryTwo = null;
    if (productDto.getL2CategoryId() != null) {
        categoryTwo = categoryTwoRepository.findById(productDto.getL2CategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category Two ID not found!"));

        if (!categoryTwo.getCategoryOne().equals(categoryOne)) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Category Two does not belong to Category One");
        }
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
       System.out.println(id); 
    ProductEntity existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Product not found!"));

    BrandEntity brand = brandRepository.findById(productDto.getBrandId())
            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Brand ID not found!"));

    checkCategories(productDto);

    MainCategoryEntity mainCategory = mainCategoryRepository.findById(productDto.getMainCategoryId())
            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Main Category ID not found!"));

    CategoryOneEntity categoryOne = categoryOneRepository.findById(productDto.getL1CategoryId())
            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category One ID not found!"));

    CategoryTwoEntity categoryTwo = null;
    if (productDto.getL2CategoryId() != null) {
        categoryTwo = categoryTwoRepository.findById(productDto.getL2CategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category Two ID not found!"));
    }

    existingProduct.setProductName(productDto.getProductName());
    existingProduct.setProductDescription(productDto.getProductDescription());
    existingProduct.setMeasuringUnit(productDto.getMeasuringUnit());
    existingProduct.setOriginalPrice(productDto.getOriginalPrice());
    existingProduct.setStockKeepingUnits(productDto.getStockKeepingUnits());
    existingProduct.setStockAvailableUnits(productDto.getStockAvailableUnits());
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

    if (existingProduct.getL1Category() == null) {
        throw new APIException(HttpStatus.BAD_REQUEST, "Product cannot be deleted because l1Category is null");
    }

    productRepository.delete(existingProduct);

    return new ProductResponseMessageDto(existingProduct.getProductName() + " deleted successfully!");
}

    @Override
    public List<ProductResponseDto> getAllProductDetails() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream()
                .map(product -> {
                    ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);
                    DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(product.getProductId());
                    if (discountEntity != null) {
                        DiscountDto discountDto = modelMapper.map(discountEntity, DiscountDto.class);
                        productResponseDto.setDiscount(discountDto);
                    }
                    productResponseDto.setCurrentPrice(getDiscountedPrice(product));
                    productResponseDto.setRate(getRate(product));
                    return productResponseDto;
                })
                .collect(Collectors.toList());
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
        DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(productEntity.getProductId());
        if (discountEntity != null) {
            DiscountDto discountDto = modelMapper.map(discountEntity, DiscountDto.class);
            productResponseDto.setDiscount(discountDto);

        }
        productResponseDto.setCurrentPrice(getDiscountedPrice(productEntity));
        productResponseDto.setRate(getRate(productEntity));

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
            DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(product.getProductId());
            if (discountEntity != null) {
                DiscountDto discountDto = modelMapper.map(discountEntity, DiscountDto.class);
                productResponseDto.setDiscount(discountDto);
            }
            productResponseDto.setCurrentPrice(getDiscountedPrice(product));
            productResponseDto.setRate(getRate(product));
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
                    DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(product.getProductId());
                    if (discountEntity != null) {
                        DiscountDto discountDto = modelMapper.map(discountEntity, DiscountDto.class);
                        productResponseDto.setDiscount(discountDto);
                    }
                    productResponseDto.setCurrentPrice(getDiscountedPrice(product));
                    productResponseDto.setRate(getRate(product));
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
                    DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(product.getProductId());
                    if (discountEntity != null) {
                        DiscountDto discountDto = modelMapper.map(discountEntity, DiscountDto.class);
                        productResponseDto.setDiscount(discountDto);
                    }
                    productResponseDto.setCurrentPrice(getDiscountedPrice(product));
                    productResponseDto.setRate(getRate(product));
                    return productResponseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByBrand(Integer brandId) {
        brandRepository.findById(brandId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Brand not found with id: "+brandId));
        List<ProductEntity> products = productRepository.findByBrandBrandId(brandId);
        return products.stream()
                .map(product -> {
                    ProductDto productResponseDto = modelMapper.map(product, ProductDto.class);
                    DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(product.getProductId());
                    if (discountEntity != null) {
                        DiscountDto discountDto = modelMapper.map(discountEntity, DiscountDto.class);
                        productResponseDto.setDiscount(discountDto);
                    }
                    productResponseDto.setCurrentPrice(getDiscountedPrice(product));
                    productResponseDto.setRate(getRate(product));
                    return productResponseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsWithoutSubs() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream()
                .map(product -> {
                    ProductDto productResponseDto = modelMapper.map(product, ProductDto.class);
                    DiscountEntity discountEntity = discountRepository.findCurrentDiscountForProduct(product.getProductId());
                    if (discountEntity != null) {
                        DiscountDto discountDto = modelMapper.map(discountEntity, DiscountDto.class);
                        productResponseDto.setDiscount(discountDto);
                    }
                    productResponseDto.setCurrentPrice(getDiscountedPrice(product));
                    productResponseDto.setRate(getRate(product));
                    return productResponseDto;
                })
                .collect(Collectors.toList());
    }
}
