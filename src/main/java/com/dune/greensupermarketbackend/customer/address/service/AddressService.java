package com.dune.greensupermarketbackend.customer.address.service;

import com.dune.greensupermarketbackend.customer.address.AddressDto;
import com.dune.greensupermarketbackend.customer.address.AddressEntity;

import java.util.List;

public interface AddressService {
    AddressDto createAddress(AddressDto addressDto);
    AddressDto getAddressByAddressId(Integer addressId);

    List<AddressDto> getAddressesByCustomerId(Integer customerId);

    AddressDto updateAddress(Integer addressId,AddressDto updatedAddress);

    void deleteAddress(Integer addressId);
}
