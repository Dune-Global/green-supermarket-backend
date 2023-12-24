package com.dune.greensupermarketbackend.product;

import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{
    List<ProductEntity> findByMainCategoryMainCategoryId(Integer mainCategoryId);
    List<ProductEntity> findByL1CategorySubCatOneId(Integer categoryOneId);
    List<ProductEntity> findByL2CategorySubCatTwoId(Integer categoryTwoId);
    List<ProductEntity> findByL2Category(CategoryTwoEntity categoryTwo);
}
