package com.dune.greensupermarketbackend.discount.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    private Integer id;
    private String discountDescription;
    private Double discountRate;
    private LocalDateTime discountStartDate;
    private LocalDateTime discountEndDate;
    private Integer productId;

}
