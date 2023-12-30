package com.dune.greensupermarketbackend.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer>{
    List<OrderEntity> findByCustomerIdOrderByOrderDateDesc(Integer customerId);
    List<OrderEntity> findByOrderStatus(String orderStatus);
    @Query(value = "SELECT * FROM customer_order WHERE customer_id = :customerId ORDER BY order_date DESC LIMIT 1", nativeQuery = true)
    Optional<OrderEntity> findLastOrderByCustomerId(@Param("customerId") Integer customerId);

    List<OrderEntity> findByCustomerIdAndOrderStatus(Integer customerId, String orderStatus);
}
