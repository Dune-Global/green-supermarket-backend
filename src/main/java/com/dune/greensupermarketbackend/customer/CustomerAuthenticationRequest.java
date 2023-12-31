package com.dune.greensupermarketbackend.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAuthenticationRequest {
    private String email;
    String password;
}
