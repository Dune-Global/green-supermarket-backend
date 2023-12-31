package com.dune.greensupermarketbackend.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateRequest {
    private String currentPassword;
    private String newPassword;
}
