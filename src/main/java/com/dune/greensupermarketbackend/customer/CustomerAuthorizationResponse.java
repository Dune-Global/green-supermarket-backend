package com.dune.greensupermarketbackend.customer;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAuthorizationResponse {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
}
