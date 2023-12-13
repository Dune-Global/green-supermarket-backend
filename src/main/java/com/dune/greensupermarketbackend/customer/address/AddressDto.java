package com.dune.greensupermarketbackend.customer.address;

import com.dune.greensupermarketbackend.customer.CustomerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
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
