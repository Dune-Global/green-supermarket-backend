package com.dune.greensupermarketbackend.admin.service.impl;

import com.dune.greensupermarketbackend.admin.dto.AdminDto;
import com.dune.greensupermarketbackend.admin.AdminEntity;
import com.dune.greensupermarketbackend.admin.AdminRepository;
import com.dune.greensupermarketbackend.admin.service.AdminService;
import com.dune.greensupermarketbackend.exception.ResourceNotFoundException;
import com.dune.greensupermarketbackend.role.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;
    private ModelMapper modelMapper;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, ModelMapper modelMapper) {
        this.adminRepository = adminRepository;
        this.modelMapper = modelMapper;
    }

    private AdminEntity checkForAdmin(String empId){
        return adminRepository.findByEmpId(empId)
                .orElseThrow(()->new ResourceNotFoundException("User not found with Employee ID "+empId)
                );
    }

    @Override
    public AdminDto getAdmin(String empId) {
        AdminEntity admin = checkForAdmin(empId);
        return modelMapper.map(admin,AdminDto.class);
    }

    @Override
    public List<AdminDto> getAllAdmins() {
        List<AdminEntity> admins = adminRepository.findAll();

        return admins.stream().map((admin)->modelMapper.map(admin,AdminDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public AdminDto updateAdmin(String empId, AdminDto adminDto) {
        AdminEntity admin = checkForAdmin(empId);

        admin.setEmpId(adminDto.getEmpId());
        admin.setFirstname(adminDto.getFirstname());
        admin.setLastname(adminDto.getLastname());
        admin.setEmail(adminDto.getEmail());
        admin.setDesignation(adminDto.getDesignation());
        admin.setPhoneNumber(adminDto.getPhoneNumber());
        admin.setRole(Role.valueOf(adminDto.getRole()));

        AdminEntity updatedAdmin = adminRepository.save(admin);
        return modelMapper.map(updatedAdmin,AdminDto.class);
    }

    @Override
    public void deleteAdmin(String empId) {
        AdminEntity admin = checkForAdmin(empId);
        adminRepository.delete(admin);
    }
}
