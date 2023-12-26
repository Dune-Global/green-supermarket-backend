package com.dune.greensupermarketbackend.news_letter.service.impl;

import com.dune.greensupermarketbackend.exception.APIException;
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

    public NewsLetterImpl(NewsLetterRepository newsLetterRepository) {
        this.newsLetterRepository = newsLetterRepository;
        this.modelMapper = new ModelMapper();
    }

    private void checkEmailAvailability(String email){
        if (newsLetterRepository.existsByEmail(email) ){
            throw new APIException(HttpStatus.BAD_REQUEST,"Email already exists");
        }
    }


    @Override
    public NewsLetterResponseDto addEmail(NewsLetterDto newsLetterDto) {

        checkEmailAvailability(newsLetterDto.getEmail());

        NewsLetterEntity newsLetterEntity = modelMapper.map(newsLetterDto, NewsLetterEntity.class);
        newsLetterRepository.save(newsLetterEntity);
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
