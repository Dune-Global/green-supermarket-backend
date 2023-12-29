package com.dune.greensupermarketbackend.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer>{
    List<OrderEntity> findByCustomerIdOrderByOrderDateDesc(Integer customerId);
    List<OrderEntity> findByOrderStatus(String orderStatus);
}
