package com.dune.greensupermarketbackend.product_rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
    @Query("SELECT r FROM RatingEntity r JOIN r.orderItem o JOIN o.product p WHERE p.productId = :productId")
    List<RatingEntity> findRatingsByProductId(@Param("productId") Integer productId);

    RatingEntity findByOrderItemOrderItemId(Integer orderItemId);

    @Query("SELECT AVG(r.rating) FROM RatingEntity r JOIN r.orderItem o JOIN o.product p WHERE p.productId = :productId")
    Double findAverageRatingByProductId(@Param("productId") Integer productId);

    @Query("SELECT COUNT(r) FROM RatingEntity r JOIN r.orderItem o JOIN o.product p WHERE p.productId = :productId")
    Long countRatingsByProductId(@Param("productId") Integer productId);

}
