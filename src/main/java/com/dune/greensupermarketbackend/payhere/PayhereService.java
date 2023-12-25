package com.dune.greensupermarketbackend.payhere;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import org.springframework.stereotype.Service;

@Service
public class PayhereService {
    public String generateHash(String merchantID, String merchantSecret, String orderID, double amount,
            String currency) {
        DecimalFormat df = new DecimalFormat("0.00");
        String amountFormatted = df.format(amount);
        String hash = getMd5(merchantID + orderID + amountFormatted + currency + getMd5(merchantSecret));
        return hash;
    }

    private String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
