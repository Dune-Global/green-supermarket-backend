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
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

    @PatchMapping("/order-status/{orderId}")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Integer orderId, @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderDto));
    }

    @GetMapping("/order-status/{orderStatus}")
    public ResponseEntity<?> findByOrderStatus(@PathVariable String orderStatus) {
        return ResponseEntity.ok(orderService.findByOrderStatus(orderStatus));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> findByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(orderService.findByCustomerId(customerId));
    }

    @PatchMapping("/payment-success/{orderId}")
    public ResponseEntity<OrderDto> payementSuccess(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.payementSuccess(orderId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

}
