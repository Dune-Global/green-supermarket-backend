package com.dune.greensupermarketbackend.order;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/order")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

    @PatchMapping("/order-status/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Integer orderId, @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderDto));
    }

    @GetMapping("/order-status/{orderStatus}")
    public ResponseEntity<List<OrderResponseDto>> findByOrderStatus(@PathVariable String orderStatus) {
        return ResponseEntity.ok(orderService.findByOrderStatus(orderStatus));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDto>> findByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(orderService.findByCustomerId(customerId));
    }

    @PatchMapping("/payment-success/{orderId}")
    public ResponseEntity<OrderResponseDto> payementSuccess(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.payementSuccess(orderId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("order/order-item/{orderId}")
    public ResponseEntity<OrderResponseDto> payhere(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.payementSuccess(orderId));
    }

}
