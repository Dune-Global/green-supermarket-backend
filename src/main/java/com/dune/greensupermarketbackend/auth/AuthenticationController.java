package com.dune.greensupermarketbackend.auth;

import com.dune.greensupermarketbackend.admin.AdminAuthenticationRequest;
import com.dune.greensupermarketbackend.admin.AdminRegisterRequest;
import com.dune.greensupermarketbackend.customer.CustomerAuthenticationRequest;
import com.dune.greensupermarketbackend.customer.CustomerRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    //Admin
    @PostMapping("admins/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody AdminRegisterRequest request) {
        return new ResponseEntity<>(service.registerAdmin(request), HttpStatus.CREATED);
    }
    @PostMapping("admins/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(@RequestBody AdminAuthenticationRequest request){
            return ResponseEntity.ok(service.authenticateAdmin(request));
    }

    //Customer
    @PostMapping("customer/register")
    public ResponseEntity<AuthenticationResponse> registerCustomer(@RequestBody CustomerRegisterRequest request){
            return new ResponseEntity<>(service.registerCustomer(request),HttpStatus.CREATED);
    }

    @PostMapping("customer/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateCustomer(@RequestBody CustomerAuthenticationRequest request){
            return ResponseEntity.ok(service.authenticateCustomer(request));
    }
}
