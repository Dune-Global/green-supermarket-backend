package com.dune.greensupermarketbackend.payhere_refund;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

@Service
public class PayHereRefundService {
    // @Value("${payhere.api.url}")
    private String payHereApiUrl = "https://sandbox.payhere.lk/merchant/v1/payment/refund";

    private final RestTemplate restTemplate;

    public PayHereRefundService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PayHereRefundResponse refundPayment(String accessToken, PayHereRefundRequest refundRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PayHereRefundRequest> requestEntity = new HttpEntity<>(refundRequest, headers);

        ResponseEntity<PayHereRefundResponse> responseEntity = restTemplate.exchange(
                payHereApiUrl + "/payment/refund",
                HttpMethod.POST,
                requestEntity,
                PayHereRefundResponse.class);

        return responseEntity.getBody();
    }
}
