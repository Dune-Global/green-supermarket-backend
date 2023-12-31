package com.dune.greensupermarketbackend.customer.address.service.impl;

import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.CustomerRepository;
import com.dune.greensupermarketbackend.customer.address.AddressDto;
import com.dune.greensupermarketbackend.customer.address.AddressEntity;
import com.dune.greensupermarketbackend.customer.address.AddressRepository;
import com.dune.greensupermarketbackend.customer.address.service.AddressService;
import com.dune.greensupermarketbackend.exception.ResourceNotFoundException;
import com.dune.greensupermarketbackend.order.OrderEntity;
import com.dune.greensupermarketbackend.order.OrderRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;
    private OrderRepository orderRepository;

    @Override
    public AddressDto createAddress(AddressDto addressDto) {

        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setLocationName(addressDto.getLocationName());
        addressEntity.setFirstName(addressDto.getFirstName());
        addressEntity.setLastName(addressDto.getLastName());
        addressEntity.setAddress(addressDto.getAddress());
        addressEntity.setPostalCode(addressDto.getPostalCode());
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setProvince(addressDto.getProvince());
        addressEntity.setEmail(addressDto.getEmail());
        addressEntity.setPhoneNumber(addressDto.getPhoneNumber());

        if(addressDto.getCustomerId() != null){
            CustomerEntity customer = customerRepository.findById(addressDto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + addressDto.getCustomerId()));

            addressEntity.setCustomer(customer);
        }

        return modelMapper.map(addressRepository.save(addressEntity),AddressDto.class);
    }

    @Override
    public AddressDto getAddressByAddressId(Integer addressId) {
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + addressId));

        return modelMapper.map(address,AddressDto.class);
    }

    @Override
    public List<AddressDto> getAddressesByCustomerId(Integer customerId) {
        List<AddressEntity> addresses = addressRepository.findByCustomerId(customerId);
        return addresses.stream().map(
                address -> modelMapper.map(address,AddressDto.class)
        ).collect(Collectors.toList());

    }

    @Override
    public AddressDto updateAddress(Integer addressId, AddressDto updatedAddress) {
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + addressId));

        address.setLocationName(updatedAddress.getLocationName());
        address.setFirstName(updatedAddress.getFirstName());
        address.setLastName(updatedAddress.getLastName());
        address.setAddress(updatedAddress.getAddress());
        address.setPostalCode(updatedAddress.getPostalCode());
        address.setCity(updatedAddress.getCity());
        address.setProvince(updatedAddress.getProvince());
        address.setEmail(updatedAddress.getEmail());
        address.setPhoneNumber(updatedAddress.getPhoneNumber());

        AddressEntity updatedAddressEntity= addressRepository.save(address);

        return modelMapper.map(updatedAddressEntity,AddressDto.class);
    }

    @Override
    public void deleteAddress(Integer addressId) {
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + addressId));

        List<OrderEntity> orders = orderRepository.findOrdersByShippingOrBillingAddressId(addressId);
        if (orders.isEmpty()) {
            addressRepository.deleteById(addressId);
        } else {
            address.setCustomer(null);
            addressRepository.save(address);
        }
    }
}
