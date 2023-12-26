package com.dune.greensupermarketbackend.payhere_refund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayHereRefundRequest {
    private String paymentId;
    private String description;
    private String authorizationToken; 
}
