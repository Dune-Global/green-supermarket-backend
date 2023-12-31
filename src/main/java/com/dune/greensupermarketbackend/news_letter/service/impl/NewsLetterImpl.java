package com.dune.greensupermarketbackend.news_letter.service.impl;

import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.mail.dto.EmailData;
import com.dune.greensupermarketbackend.mail.service.MailService;
import com.dune.greensupermarketbackend.news_letter.NewsLetterEntity;
import com.dune.greensupermarketbackend.news_letter.NewsLetterRepository;
import com.dune.greensupermarketbackend.news_letter.dto.NewsLetterDto;
import com.dune.greensupermarketbackend.news_letter.dto.NewsLetterResponseDto;
import com.dune.greensupermarketbackend.news_letter.service.NewsLetterService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsLetterImpl implements NewsLetterService {
    private final NewsLetterRepository newsLetterRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;

    public NewsLetterImpl(NewsLetterRepository newsLetterRepository, MailService mailService) {
        this.newsLetterRepository = newsLetterRepository;
        this.mailService = mailService;
        this.modelMapper = new ModelMapper();
    }

    private void checkEmailAvailability(String email){
        if (newsLetterRepository.existsByEmail(email) ){
            throw new APIException(HttpStatus.BAD_REQUEST,"Email already exists");
        }
    }


    @Override
    public NewsLetterResponseDto addEmail(NewsLetterDto newsLetterDto) {
        if(newsLetterDto.getEmail().isEmpty()){
            throw new APIException(HttpStatus.BAD_REQUEST,"Please enter your email");
        }

        checkEmailAvailability(newsLetterDto.getEmail());

        NewsLetterEntity newsLetterEntity = modelMapper.map(newsLetterDto, NewsLetterEntity.class);
        newsLetterRepository.save(newsLetterEntity);

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<link href='https://fonts.googleapis.com/css2?family=Poppins&display=swap' rel='stylesheet'>" +
                        "<style>" +
                        "body { font-family: 'Poppins', sans-serif; }" +
                        "h3 { color: #191919; }" +
                        "p { color: rgb(71, 71, 71); }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<h3>Thank you for subscribing to our newsletter!</h3>" +
                        "<p>Hi,</p>" +
                        "<p>We're excited to have you on board. You can look forward to updates, exclusive offers, and much more delivered straight to your inbox.</p>" +
                        "<p>Thank you,</p>" +
                        "<p><span style=\"color: #00b207; font-weight: 500\">GREEN </span><span style=\"color: black; font-weight: 500\">SUPERMARKET</span></p>" +
                        "</body>" +
                        "</html>";

        EmailData emailData = new EmailData(
                newsLetterDto.getEmail(),
                newsLetterDto.getEmail(),
                "Welcome to Green Supermarket!",
                htmlBody
        );

        mailService.sendMail(emailData);

        return new NewsLetterResponseDto("Email added successfully", newsLetterDto.getEmail());
    }

    @Override
    public List<NewsLetterDto> getAllEmails() {
        List<NewsLetterEntity> newsLetterEntities = newsLetterRepository.findAll();
        return newsLetterEntities.stream().map(
                newsLetterEntity -> modelMapper.map(newsLetterEntity, NewsLetterDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NewsLetterResponseDto deleteEmail(NewsLetterDto newsLetterDto) {
        if (!newsLetterRepository.existsByEmail(newsLetterDto.getEmail()) ){
            throw new APIException(HttpStatus.BAD_REQUEST,"Email not exists");
        }
        newsLetterRepository.deleteByEmail(newsLetterDto.getEmail());
        return new NewsLetterResponseDto("Email deleted successfully", newsLetterDto.getEmail());
    }
}
