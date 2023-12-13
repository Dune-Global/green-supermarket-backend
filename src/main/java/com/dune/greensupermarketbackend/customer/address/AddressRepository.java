package com.dune.greensupermarketbackend.customer.address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity,Integer> {
    List<AddressEntity> findByCustomerId(Integer customerId);
}
