package com.dune.greensupermarketbackend.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String imageUrl;
}
