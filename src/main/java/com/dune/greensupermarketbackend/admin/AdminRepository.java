package com.dune.greensupermarketbackend.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
    Optional<AdminEntity> findByEmpId(String empId);

    Boolean existsByEmpId(String empId);

    Boolean existsByEmail(String email);
}
