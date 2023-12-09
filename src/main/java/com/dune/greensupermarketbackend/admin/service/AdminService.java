package com.dune.greensupermarketbackend.admin.service;

import com.dune.greensupermarketbackend.admin.AdminDto;

import java.util.List;

public interface AdminService {
    AdminDto getAdmin(String empId);
    List<AdminDto> getAllAdmins();
    AdminDto updateAdmin(String empId,AdminDto adminDto);
    void deleteAdmin(String empId);
}
