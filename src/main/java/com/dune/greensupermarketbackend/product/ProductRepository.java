package com.dune.greensupermarketbackend.product;

import com.dune.greensupermarketbackend.brand.BrandEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_one.CategoryOneEntity;
import com.dune.greensupermarketbackend.category.sub_category.category_two.CategoryTwoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{
    List<ProductEntity> findByMainCategoryMainCategoryId(Integer mainCategoryId);
    List<ProductEntity> findByL1CategorySubCatOneId(Integer categoryOneId);
    List<ProductEntity> findByL2CategorySubCatTwoId(Integer categoryTwoId);
    List<ProductEntity> findByBrandBrandId(Integer brandId);
    List<ProductEntity> findByProductNameContainingIgnoreCase(String productName);

    @Query("SELECT DISTINCT p.brand FROM ProductEntity p WHERE p.mainCategory.mainCategoryId = :mainCategoryId")
    List<BrandEntity> findDistinctBrandsByMainCategoryId(@Param("mainCategoryId") Integer mainCategoryId);

    @Query("SELECT DISTINCT p.brand FROM ProductEntity p WHERE p.l1Category.subCatOneId = :subCatOneId")
    List<BrandEntity> findDistinctBrandsBySubCatOneId(@Param("subCatOneId") Integer subCatOneId);

    @Query("SELECT p FROM ProductEntity p WHERE (:mainCatId is null or p.mainCategory.mainCategoryId = :mainCatId) and (:subCatId is null or p.l1Category.subCatOneId = :subCatId) and (:minPrice is null or p.originalPrice >= :minPrice) and (:maxPrice is null or p.originalPrice <= :maxPrice)")
    List<ProductEntity> findProductsByFilter(@Param("mainCatId") Integer mainCatId, @Param("subCatId") Integer subCatId, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("SELECT p " +
            "FROM OrderEntity o " +
            "JOIN o.orderItems oi " +
            "JOIN oi.product p " +
            "WHERE o.paymentStatus = 'Success' " +
            "GROUP BY p " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<ProductEntity> findTop10BestSellingProducts();
}
