package com.dune.greensupermarketbackend.auth;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.admin.AdminAuthenticationRequest;
import com.dune.greensupermarketbackend.admin.AdminAuthorizationResponse;
import com.dune.greensupermarketbackend.admin.AdminRegisterDto;
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

    //Admin
    @PostMapping("admins/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody AdminRegisterDto request) {
        return new ResponseEntity<>(service.registerAdmin(request), HttpStatus.CREATED);
    }
    @PostMapping("admins/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(@RequestBody AdminAuthenticationRequest request){
            return ResponseEntity.ok(service.authenticateAdmin(request));
    }

    @GetMapping("admins/auth")
    public ResponseEntity<AdminAuthorizationResponse> authorizeAdmin(@RequestHeader("Authorization") String token){

    // Remove the "Bearer " prefix from the token
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
    }
    return ResponseEntity.ok(service.authorizeAdmin(token));
    }

    //Customer
    @PostMapping("customers/register")
    public ResponseEntity<AuthenticationResponse> registerCustomer(@RequestBody CustomerRegisterDto request){
            return new ResponseEntity<>(service.registerCustomer(request),HttpStatus.CREATED);
    }

    @PostMapping("customers/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateCustomer(@RequestBody CustomerAuthenticationRequest request){
            return ResponseEntity.ok(service.authenticateCustomer(request));
    }

    @GetMapping("customers/auth")
    public ResponseEntity<CustomerAuthorizationResponse> authorizeCustomer(@RequestHeader("Authorization") String token){

    // Remove the "Bearer " prefix from the token
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
    }
    return ResponseEntity.ok(service.authorizeCustomer(token));
    }
}
