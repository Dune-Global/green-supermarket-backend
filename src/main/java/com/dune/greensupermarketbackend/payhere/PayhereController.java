package com.dune.greensupermarketbackend.payhere;

import com.dune.greensupermarketbackend.ApiVersionConfig;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/payhere")
@AllArgsConstructor
public class PayhereController {
    @Autowired
    private PayhereService payhereService;

    @PostMapping("generate-hash")
    public ResponseEntity<Map<String, String>> generateHash(@RequestBody PayhereEntity payhereEntity) {
        String hash = payhereService.generateHash(
                payhereEntity.getMerchantID(),
                payhereEntity.getMerchantSecret(),
                payhereEntity.getOrderID(),
                payhereEntity.getAmount(),
                payhereEntity.getCurrency());
        Map<String, String> response = new HashMap<>();
        response.put("hash", hash);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
