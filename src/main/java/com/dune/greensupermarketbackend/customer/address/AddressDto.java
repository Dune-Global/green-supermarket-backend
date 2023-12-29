package com.dune.greensupermarketbackend.customer.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Integer id;
    private String locationName;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private String province;
    private String email;
    private String phoneNumber;
    private Integer customerId;
}
