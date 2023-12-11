package com.dune.greensupermarketbackend.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAuthorizationResponse {
    private String empId;
    private String firstname;
    private String lastname;
    private String email;
    private String roles;
}