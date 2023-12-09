package com.dune.greensupermarketbackend.customer.service.impl;

import com.dune.greensupermarketbackend.customer.CustomerDto;
import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.CustomerRepository;
import com.dune.greensupermarketbackend.customer.service.CustomerService;
import com.dune.greensupermarketbackend.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
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

        CustomerEntity updatedCustomer = customerRepository.save(customer);
        return modelMapper.map(updatedCustomer,CustomerDto.class);
    }

    @Override
    public void deleteCustomer(Integer id) {
        checkForCustomer(id);
        customerRepository.deleteById(id);
    }
}
