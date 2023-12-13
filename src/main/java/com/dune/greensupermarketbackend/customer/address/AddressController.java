package com.dune.greensupermarketbackend.customer.address;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.customer.address.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/customers/addresses")
@AllArgsConstructor
public class AddressController {
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto){
        AddressDto savedAddress = addressService.createAddress(addressDto);

        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @GetMapping("{address-id}")
    public ResponseEntity<AddressDto> getAddressByAddressId(@PathVariable("address-id") Integer addressId){
        AddressDto addressDto = addressService.getAddressByAddressId(addressId);
        return ResponseEntity.ok(addressDto);
    }

    @GetMapping("customer/{customer-id}")
    public ResponseEntity<List<AddressDto>> getAddressByCustomerId(@PathVariable("customer-id") Integer customerId){
        List<AddressDto> addressDtos = addressService.getAddressesByCustomerId(customerId);
        return ResponseEntity.ok(addressDtos);
    }

    @PutMapping("{address-id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable("address-id") Integer addressId,@RequestBody AddressDto updatedAddress){
        AddressDto addressDto = addressService.updateAddress(addressId,updatedAddress);
        return ResponseEntity.ok(addressDto);
    }

    @DeleteMapping("/delete-address/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") Integer addressId){
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok("Address Deleted successfully");
    }
}
