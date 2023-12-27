package com.dune.greensupermarketbackend.customer.service.impl;

import com.dune.greensupermarketbackend.admin.AdminEntity;
import com.dune.greensupermarketbackend.auth.PasswordUpdateRequest;
import com.dune.greensupermarketbackend.customer.CustomerDto;
import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.CustomerRepository;
import com.dune.greensupermarketbackend.customer.service.CustomerService;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private CustomerEntity checkForCustomer(Integer id){
        return customerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Customer not found with "+ id ));
    }

    @Override
    public CustomerDto getCustomer(Integer id) {
        CustomerEntity customer = checkForCustomer(id);
        return modelMapper.map(customer, CustomerDto.class);
    }

    @Override
    public List<CustomerDto> getAllCustomer() {
        List<CustomerEntity> customers = customerRepository.findAll();

        return customers.stream().map((customer)->modelMapper.map(customer, CustomerDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public CustomerDto updateCustomer(Integer id, CustomerDto customerDto) {
        CustomerEntity customer = checkForCustomer(id);

        customer.setFirstname(customerDto.getFirstname());
        customer.setLastname(customerDto.getLastname());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setImageUrl(customerDto.getImageUrl());

        CustomerEntity updatedCustomer = customerRepository.save(customer);
        return modelMapper.map(updatedCustomer,CustomerDto.class);
    }

    @Override
    public void updatePassword(Integer customerId, PasswordUpdateRequest passwordUpdateRequest) {

        CustomerEntity customer = checkForCustomer(customerId);
        if (!passwordEncoder.matches(passwordUpdateRequest.getCurrentPassword(), customer.getPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST,"Current password does not match");
        }
        customer.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Integer id) {
        checkForCustomer(id);
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDto findByCartId(Integer cartId) {
        CustomerEntity customer = customerRepository.findByCartCartId(cartId)
                .orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"Customer with cart Id "+ cartId +" not founf")
                );
        return modelMapper.map(customer,CustomerDto.class);
    }
}
