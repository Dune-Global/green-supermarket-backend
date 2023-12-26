package com.dune.greensupermarketbackend.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountRepository extends JpaRepository<DiscountEntity,Integer> {
    List<DiscountEntity> findByProductProductId(Integer productId);

    @Query("SELECT d FROM DiscountEntity d WHERE CURRENT_TIMESTAMP BETWEEN d.startDate AND d.endDate")
    List<DiscountEntity> findCurrentDiscounts();

    @Query("SELECT d FROM DiscountEntity d WHERE CURRENT_TIMESTAMP BETWEEN d.startDate AND d.endDate AND d.product.productId = :productId")
    DiscountEntity findCurrentDiscountForProduct(Integer productId);

    @Query("SELECT d FROM DiscountEntity d WHERE d.product.productId = :productId AND ((d.startDate BETWEEN :startDate AND :endDate) OR (d.endDate BETWEEN :startDate AND :endDate))")
    List<DiscountEntity> findDiscountsForProductInTimeRange(Integer productId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT d FROM DiscountEntity d WHERE d.product.productId = :productId AND d.id != :discountId AND ((d.startDate BETWEEN :startDate AND :endDate) OR (d.endDate BETWEEN :startDate AND :endDate))")
    List<DiscountEntity> findDiscountsForProductInTimeRange(Integer productId, LocalDateTime startDate, LocalDateTime endDate, Integer discountId);
}