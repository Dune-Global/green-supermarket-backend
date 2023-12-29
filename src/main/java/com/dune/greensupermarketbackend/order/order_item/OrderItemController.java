package com.dune.greensupermarketbackend.order.order_item;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.order.order_item.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/order-item")
@AllArgsConstructor
public class OrderItemController {

    private OrderItemService orderItemService;

    @GetMapping("{order-item-id}")
    public ResponseEntity<OrderItemDto> getByOrderItemId(@PathVariable("order-item-id") Integer orderItemId) {
        return ResponseEntity.ok(orderItemService.getByOrderItemId(orderItemId));
    }

    @GetMapping("order/{order-id}")
    public ResponseEntity<List<OrderItemDto>> getByOrderId(@PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(orderItemService.getByOrderId(orderId));
    }


}
