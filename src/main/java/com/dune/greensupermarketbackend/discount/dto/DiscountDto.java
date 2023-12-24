package com.dune.greensupermarketbackend.discount.dto;

import java.time.LocalDateTime;

public class DiscountDto {
    private String discountDescription;
    private String discountRate;
    private LocalDateTime discountStartDate;
    private LocalDateTime discountEndDate;
    private Integer productId;

}
