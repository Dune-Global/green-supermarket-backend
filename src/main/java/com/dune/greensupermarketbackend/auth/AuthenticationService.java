package com.dune.greensupermarketbackend.auth;

import com.dune.greensupermarketbackend.admin.*;
import com.dune.greensupermarketbackend.config.JwtService;
import com.dune.greensupermarketbackend.customer.*;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private Map<String,Object> customerExtraClaims(CustomerEntity customer) {
        Map<String, Object> customerExtraClaims = new HashMap<>();
        customerExtraClaims.put("id", customer.getId());
        customerExtraClaims.put("firstname", customer.getFirstname());
        customerExtraClaims.put("lastname", customer.getLastname());
        customerExtraClaims.put("email", customer.getEmail());
        customerExtraClaims.put("role", customer.getRole().toString());

        return customerExtraClaims;
    }

    //Admin
    public AuthenticationResponse registerAdmin(AdminRegisterDto request){
        //Check EmpId exist
        if(adminRepository.existsByEmpId(request.getEmpId())){
            throw new APIException(HttpStatus.BAD_REQUEST,"Employee ID Already exists.");
        }
        if(adminRepository.existsByEmail(request.getEmail())){
            throw new APIException(HttpStatus.BAD_REQUEST,"Employee Email Already exists.");
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
//        var jwtToken = jwtService.generateToken(admin);
        return AuthenticationResponse.builder()
//                .token(jwtToken)
                .message("Register Successfully")
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

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("empId", admin.getEmpId());
        extraClaims.put("firstname", admin.getFirstname());
        extraClaims.put("lastname", admin.getLastname());
        extraClaims.put("email", admin.getEmail());
        extraClaims.put("roles", admin.getRole());

        var jwtToken = jwtService.generateToken(extraClaims,admin);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Login Successful")
                .build();
    }

    //Customer
    public AuthenticationResponse registerCustomer(CustomerRegisterDto request){
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

        Map<String, Object> customerExtraClaims = customerExtraClaims(customer);
        var jwtToken = jwtService.generateToken(customerExtraClaims, customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Register Successfully")
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

        Map<String, Object> customerExtraClaims = customerExtraClaims(customer);
        var jwtToken = jwtService.generateToken(customerExtraClaims, customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Login Successfully")
                .build();
    }

}
