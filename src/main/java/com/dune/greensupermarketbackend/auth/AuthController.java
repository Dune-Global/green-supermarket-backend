package com.dune.greensupermarketbackend.auth;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.admin.dto.AdminAuthenticationRequest;
import com.dune.greensupermarketbackend.admin.dto.AdminAuthorizationResponse;
import com.dune.greensupermarketbackend.admin.dto.AdminRegisterDto;
import com.dune.greensupermarketbackend.customer.CustomerAuthenticationRequest;
import com.dune.greensupermarketbackend.customer.CustomerAuthorizationResponse;
import com.dune.greensupermarketbackend.customer.CustomerRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/"+ ApiVersionConfig.API_VERSION)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    //Admin sign up
    @PostMapping("admins/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody AdminRegisterDto request) {
        return new ResponseEntity<>(service.registerAdmin(request), HttpStatus.CREATED);
    }

    //Admin sign in
    @PostMapping("admins/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(@RequestBody AdminAuthenticationRequest request){
            return ResponseEntity.ok(service.authenticateAdmin(request));
    }

    //Decode JWT token
    @GetMapping("admins/auth")
    public ResponseEntity<AdminAuthorizationResponse> authorizeAdmin(@RequestHeader("Authorization") String token){

    // Remove the "Bearer " prefix from the token
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
    }
    return ResponseEntity.ok(service.authorizeAdmin(token));
    }

    //Customer sign up
    @PostMapping("customers/register")
    public ResponseEntity<AuthenticationResponse> registerCustomer(@RequestBody CustomerRegisterDto request){
            return new ResponseEntity<>(service.registerCustomer(request),HttpStatus.CREATED);
    }

    //Customer sign in
    @PostMapping("customers/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateCustomer(@RequestBody CustomerAuthenticationRequest request){
            return ResponseEntity.ok(service.authenticateCustomer(request));
    }

    //Decode JWT token
    @GetMapping("customers/auth")
    public ResponseEntity<CustomerAuthorizationResponse> authorizeCustomer(@RequestHeader("Authorization") String token){

    // Remove the "Bearer " prefix from the token
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
    }
    return ResponseEntity.ok(service.authorizeCustomer(token));
    }
}
