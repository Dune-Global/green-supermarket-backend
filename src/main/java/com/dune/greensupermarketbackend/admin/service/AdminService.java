package com.dune.greensupermarketbackend.admin.service;

import com.dune.greensupermarketbackend.admin.dto.AdminDto;
import com.dune.greensupermarketbackend.auth.PasswordUpdateRequest;

import java.util.List;

public interface AdminService {
    AdminDto getAdmin(String empId);
    List<AdminDto> getAllAdmins();
    AdminDto updateAdmin(String empId,AdminDto adminDto);
    void updatePassword(String empId, PasswordUpdateRequest passwordUpdateRequest);
    void deleteAdmin(String empId);
}
