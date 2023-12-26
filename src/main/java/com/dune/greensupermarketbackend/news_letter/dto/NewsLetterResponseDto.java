package com.dune.greensupermarketbackend.news_letter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsLetterResponseDto{
    private String message;
    private String email;
}
