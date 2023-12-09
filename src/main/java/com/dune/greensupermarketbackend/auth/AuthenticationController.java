package com.dune.greensupermarketbackend.auth;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.admin.AdminAuthenticationRequest;
import com.dune.greensupermarketbackend.admin.AdminDto;
import com.dune.greensupermarketbackend.admin.AdminRegisterDto;
import com.dune.greensupermarketbackend.customer.CustomerAuthenticationRequest;
import com.dune.greensupermarketbackend.customer.CustomerDto;
import com.dune.greensupermarketbackend.customer.CustomerRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/"+ ApiVersionConfig.API_VERSION +"/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    //Admin
    @PostMapping("admins/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody AdminRegisterDto request) {
        return new ResponseEntity<>(service.registerAdmin(request), HttpStatus.CREATED);
    }
    @PostMapping("admins/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(@RequestBody AdminAuthenticationRequest request){
            return ResponseEntity.ok(service.authenticateAdmin(request));
    }

    //Customer
    @PostMapping("customer/register")
    public ResponseEntity<AuthenticationResponse> registerCustomer(@RequestBody CustomerRegisterDto request){
            return new ResponseEntity<>(service.registerCustomer(request),HttpStatus.CREATED);
    }

    @PostMapping("customer/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateCustomer(@RequestBody CustomerAuthenticationRequest request){
            return ResponseEntity.ok(service.authenticateCustomer(request));
    }
}
