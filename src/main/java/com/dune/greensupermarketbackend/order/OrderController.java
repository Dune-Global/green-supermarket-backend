package com.dune.greensupermarketbackend.order;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Integer orderId, @RequestBody String orderStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderStatus));
    }

    @PatchMapping("/payment-status/{orderId}")
    public ResponseEntity<OrderDto> updatePaymentStatus(@PathVariable Integer orderId, @RequestBody String paymentStatus) {
        return ResponseEntity.ok(orderService.updatePaymentStatus(orderId, paymentStatus));
    }

    @GetMapping("/order-status/{orderStatus}")
    public ResponseEntity<?> findByOrderStatus(@PathVariable String orderStatus) {
        return ResponseEntity.ok(orderService.findByOrderStatus(orderStatus));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> findByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(orderService.findByCustomerId(customerId));
    }

}
