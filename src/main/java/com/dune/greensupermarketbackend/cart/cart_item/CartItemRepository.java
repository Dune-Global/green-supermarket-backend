package com.dune.greensupermarketbackend.cart.cart_item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {
    Optional<List<CartItemEntity>> findAllByCartCartId(Integer cartId);
}
