package com.dune.greensupermarketbackend.order.order_item;

import com.dune.greensupermarketbackend.product_rating.dto.RatingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Integer orderItemId;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double totalAmount;
    private RatingDto rating;
}