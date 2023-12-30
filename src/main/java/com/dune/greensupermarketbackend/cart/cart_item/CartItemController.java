package com.dune.greensupermarketbackend.cart.cart_item;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.cart.dto.CartWithItemsDto;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemRequestDto;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.cart.cart_item.service.CartItemService;
import com.dune.greensupermarketbackend.cart.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/cart")
@AllArgsConstructor
public class CartItemController {
    private CartItemService cartItemService;
    private CartService cartService;

    // Get cart items by cart id
    @GetMapping("{cartId}")
    public ResponseEntity<List<CartItemResponseDto>> getCartItemsByCartId(@PathVariable("cartId") Integer cartId) {
        List<CartItemResponseDto> list = cartItemService.getCartItems(cartId);
        return ResponseEntity.ok(list);
    }

    //Get Cart Items with total
    @GetMapping("total/{cartId}")
    public ResponseEntity<CartWithItemsDto> getCartItemsWithTotal(@PathVariable("cartId") Integer cartId) {
        CartWithItemsDto cartItemResponseDto = cartService.getCartWithItems(cartId);
        return ResponseEntity.ok(cartItemResponseDto);
    }


    //Add items to cart
    @PostMapping("add-to-cart")
    public ResponseEntity<CartItemResponseDto> addToCart(@RequestBody CartItemRequestDto cartItemRequestDto) {
        CartItemResponseDto cartItemResponseDto = cartItemService.addToCart(cartItemRequestDto);
        return ResponseEntity.ok(cartItemResponseDto);
    }

    // Remove item from cart
    @DeleteMapping("remove-from-cart/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable("cartItemId") Integer cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @PatchMapping("update-quantity/{cartItemId}")
    public ResponseEntity<CartItemResponseDto> updateQuantity(@PathVariable("cartItemId") Integer cartItemId, @RequestBody CartItemRequestDto cartItemRequestDto) {
        CartItemResponseDto cartItemResponseDto = cartItemService.updateQuantity(cartItemId, cartItemRequestDto);
        return ResponseEntity.ok(cartItemResponseDto);
    }
}
