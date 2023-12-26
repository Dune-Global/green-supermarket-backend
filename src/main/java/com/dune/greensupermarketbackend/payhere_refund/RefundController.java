package com.dune.greensupermarketbackend.payhere_refund;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dune.greensupermarketbackend.ApiVersionConfig;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/payhere-refund")
public class RefundController {
    private final PayHereRefundService payHereRefundService;

    @Autowired
    public RefundController(PayHereRefundService payHereRefundService) {
        this.payHereRefundService = payHereRefundService;
    }

    @PostMapping("/payment")
    public ResponseEntity<PayHereRefundResponse> refundPayment(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody PayHereRefundRequest refundRequest) {
        PayHereRefundResponse refundResponse = payHereRefundService.refundPayment(accessToken, refundRequest);

        if (refundResponse != null) {
            return ResponseEntity.ok(refundResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
