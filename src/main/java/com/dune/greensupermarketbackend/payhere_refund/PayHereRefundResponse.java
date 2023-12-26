package com.dune.greensupermarketbackend.payhere_refund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayHereRefundResponse {
    private int status;
    private String msg;
    private Object data;
}
