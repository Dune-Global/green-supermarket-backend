package com.dune.greensupermarketbackend.cart;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.cart.dto.CartDto;
import com.dune.greensupermarketbackend.cart.service.CartService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/cart")
@AllArgsConstructor
public class CartContoller {
    private CartService cartService;

    // Create a new Cart
    @GetMapping("/new-cart")
    public ResponseEntity<CartDto> createCart() {
        CartDto cartDto = cartService.createCart();
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}
