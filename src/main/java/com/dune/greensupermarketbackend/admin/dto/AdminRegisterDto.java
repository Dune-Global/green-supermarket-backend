package com.dune.greensupermarketbackend.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterDto {
    private String empId;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String designation;
    private String phoneNumber;
    private String role;
}
