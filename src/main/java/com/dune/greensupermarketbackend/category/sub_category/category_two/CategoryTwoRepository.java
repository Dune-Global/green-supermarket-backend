package com.dune.greensupermarketbackend.category.sub_category.category_two;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryTwoRepository extends JpaRepository<CategoryTwoEntity, Integer>{
    Boolean existsByCategoryOneSubCatOneId(Integer subCatOneId);
    List<CategoryTwoEntity> findByCategoryOneSubCatOneId(Integer subCatOneId);
}
