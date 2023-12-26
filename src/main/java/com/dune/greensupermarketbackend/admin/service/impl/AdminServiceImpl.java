package com.dune.greensupermarketbackend.admin.service.impl;

import com.dune.greensupermarketbackend.admin.dto.AdminDto;
import com.dune.greensupermarketbackend.admin.AdminEntity;
import com.dune.greensupermarketbackend.admin.AdminRepository;
import com.dune.greensupermarketbackend.admin.service.AdminService;
import com.dune.greensupermarketbackend.auth.PasswordUpdateRequest;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.admin.RoleEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, ModelMapper modelMapper,PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private AdminEntity checkForAdmin(String empId){
        return adminRepository.findByEmpId(empId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"User not found with Employee ID "+empId)
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

        admin.setFirstname(adminDto.getFirstname());
        admin.setLastname(adminDto.getLastname());
        admin.setEmail(adminDto.getEmail());
        admin.setDesignation(adminDto.getDesignation());
        admin.setPhoneNumber(adminDto.getPhoneNumber());
        admin.setRole(RoleEnum.valueOf(adminDto.getRole()));

        AdminEntity updatedAdmin = adminRepository.save(admin);
        return modelMapper.map(updatedAdmin,AdminDto.class);
    }

    @Override
    public void updatePassword(String empId, PasswordUpdateRequest passwordUpdateRequest) {
        AdminEntity admin = checkForAdmin(empId);
        if (!passwordEncoder.matches(passwordUpdateRequest.getCurrentPassword(), admin.getPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST,"Current password does not match");
        }
        admin.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        adminRepository.save(admin);
    }

    @Override
    public void deleteAdmin(String empId) {
        AdminEntity admin = checkForAdmin(empId);
        adminRepository.delete(admin);
    }
}
