package com.dune.greensupermarketbackend.mail.service.impl;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dune.greensupermarketbackend.mail.dto.EmailData;
import com.dune.greensupermarketbackend.mail.service.MailService;

import jakarta.mail.internet.MimeMessage;
import com.dune.greensupermarketbackend.mail.dto.EmailData;
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public EmailData sendMail(EmailData emailData) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailData.getTo());
            mimeMessageHelper.setCc(emailData.getCc());
            mimeMessageHelper.setSubject(emailData.getSubject());
            mimeMessageHelper.setText(emailData.getBody());

            javaMailSender.send(mimeMessage);

            return emailData;

        } catch (Exception e) {
            throw new RuntimeException("Failed to send mail", e);
        }
    }

}
