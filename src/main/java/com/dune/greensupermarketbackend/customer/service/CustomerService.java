package com.dune.greensupermarketbackend.customer.service;

import com.dune.greensupermarketbackend.auth.PasswordUpdateRequest;
import com.dune.greensupermarketbackend.customer.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto getCustomer(Integer id);
    List<CustomerDto> getAllCustomer();
    CustomerDto updateCustomer(Integer id, CustomerDto customerDto);
    void updatePassword(Integer customerId, PasswordUpdateRequest passwordUpdateRequest);
    void deleteCustomer(Integer id);
    CustomerDto findByCartId(Integer cartId);
}
