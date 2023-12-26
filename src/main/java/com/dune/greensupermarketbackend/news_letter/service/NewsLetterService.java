package com.dune.greensupermarketbackend.news_letter.service;

import com.dune.greensupermarketbackend.news_letter.dto.NewsLetterDto;
import com.dune.greensupermarketbackend.news_letter.dto.NewsLetterResponseDto;

import java.util.List;

public interface NewsLetterService {
    NewsLetterResponseDto addEmail(NewsLetterDto newsLetterDto);
    List<NewsLetterDto> getAllEmails();
    NewsLetterResponseDto deleteEmail(NewsLetterDto newsLetterDto);
}
