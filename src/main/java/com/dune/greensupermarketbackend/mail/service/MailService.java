package com.dune.greensupermarketbackend.mail.service;

import org.springframework.web.multipart.MultipartFile;

import com.dune.greensupermarketbackend.mail.dto.EmailData;

public interface MailService {

    EmailData sendMail(EmailData emailData);

}
