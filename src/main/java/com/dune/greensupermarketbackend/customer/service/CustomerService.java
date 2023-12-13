package com.dune.greensupermarketbackend.customer.service;

import com.dune.greensupermarketbackend.customer.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto getCustomer(Integer id);
    List<CustomerDto> getAllCustomer();
    CustomerDto updateCustomer(Integer id, CustomerDto customerDto);
    void deleteCustomer(Integer id);
}
