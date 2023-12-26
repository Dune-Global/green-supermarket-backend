package com.dune.greensupermarketbackend.news_letter;

import com.dune.greensupermarketbackend.ApiVersionConfig;
import com.dune.greensupermarketbackend.news_letter.dto.NewsLetterDto;
import com.dune.greensupermarketbackend.news_letter.dto.NewsLetterResponseDto;
import com.dune.greensupermarketbackend.news_letter.service.NewsLetterService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/" + ApiVersionConfig.API_VERSION + "/news-letter")
@AllArgsConstructor
public class NewsLetterController {
    private NewsLetterService newsLetterService;

    @PostMapping("/add-email")
    public ResponseEntity<NewsLetterResponseDto> addEmail(@RequestBody NewsLetterDto newsLetterDto) {
        NewsLetterResponseDto newsLetterResponseDto = newsLetterService.addEmail(newsLetterDto);
        return ResponseEntity.ok(newsLetterResponseDto);
    }

    @GetMapping("/get-all-emails")
    public ResponseEntity<List<NewsLetterDto>> getAllEmails() {
        List<NewsLetterDto> registeredEmails = newsLetterService.getAllEmails();
        return new ResponseEntity<>(registeredEmails, HttpStatus.OK);
    }

    @DeleteMapping("delete")
    public ResponseEntity<NewsLetterResponseDto> deleteEmail(@RequestBody NewsLetterDto newsLetterDto) {
        NewsLetterResponseDto newsLetterResponseDto = newsLetterService.deleteEmail(newsLetterDto);
        return new ResponseEntity<>(newsLetterResponseDto, HttpStatus.OK);
    }
}
