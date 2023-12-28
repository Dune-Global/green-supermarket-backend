package com.dune.greensupermarketbackend.mail;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.mail.dto.EmailData;
import com.dune.greensupermarketbackend.mail.service.MailService;

import lombok.AllArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/mail")
@AllArgsConstructor
public class MailController {

    private MailService mailService;

    @PostMapping("send")
    public ResponseEntity<EmailData> sendMail(@RequestBody EmailData emailData) {
        EmailData email = mailService.sendMail(emailData);
        return ResponseEntity.ok(email); 

    }
}
