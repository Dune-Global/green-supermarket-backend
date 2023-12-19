package com.dune.greensupermarketbackend.category.sub_category.category_one;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryOneRepository extends JpaRepository<CategoryOneEntity, Integer>{
    List<CategoryOneEntity> findByMainCategoryMainCategoryId(Integer mainCategoryId);
    //Boolean existsByMainCategoryMainCategoryId(Integer mainCategoryId);
}