package com.dune.greensupermarketbackend.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<CustomerEntity> findByCartCartId(Integer cartId);
}