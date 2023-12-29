package com.dune.greensupermarketbackend.order.order_item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity,Integer> {
    List<OrderItemEntity> findAllByOrderOrderId(Integer orderId);

}
