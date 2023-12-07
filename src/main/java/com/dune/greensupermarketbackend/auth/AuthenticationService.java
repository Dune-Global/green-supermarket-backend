package com.dune.greensupermarketbackend.auth;

import com.dune.greensupermarketbackend.admin.AdminAuthenticationRequest;
import com.dune.greensupermarketbackend.admin.AdminEntity;
import com.dune.greensupermarketbackend.admin.AdminRegisterRequest;
import com.dune.greensupermarketbackend.admin.AdminRepository;
import com.dune.greensupermarketbackend.config.JwtService;
import com.dune.greensupermarketbackend.customer.CustomerAuthenticationRequest;
import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.CustomerRegisterRequest;
import com.dune.greensupermarketbackend.customer.CustomerRepository;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //Admin
    public AuthenticationResponse registerAdmin(AdminRegisterRequest request){
        //Check EmpId exist
        if(adminRepository.existsByEmpId(request.getEmpId())){
            throw new APIException(HttpStatus.BAD_REQUEST,"Employee ID Already exists.");
        }

        var admin = AdminEntity.builder()
                .empId(request.getEmpId())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .designation(request.getDesignation())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .build();
        adminRepository.save(admin);
        var jwtToken = jwtService.generateToken(admin);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateAdmin(AdminAuthenticationRequest request){
        var admin = adminRepository.findByEmpId(request.getEmpId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Invalid empId or password"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST,"Invalid empId or password");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmpId(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(admin);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //Customer
    public AuthenticationResponse registerCustomer(CustomerRegisterRequest request){
        if(customerRepository.existsByEmail(request.getEmail())){
            throw new APIException(HttpStatus.BAD_REQUEST,"Email Already exists");
        }
        var customer = CustomerEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.CUSTOMER)
                .build();
        customerRepository.save(customer);
        var jwtToken = jwtService.generateToken((customer));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateCustomer(CustomerAuthenticationRequest request){
        var customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,"Invalid Email or password"));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST,"Invalid Email or password");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
