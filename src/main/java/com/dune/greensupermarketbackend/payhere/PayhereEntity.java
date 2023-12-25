package com.dune.greensupermarketbackend.payhere;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayhereEntity {
    private String merchantID;
    private String merchantSecret;
    private String orderID;
    private double amount;
    private String currency; 
}
