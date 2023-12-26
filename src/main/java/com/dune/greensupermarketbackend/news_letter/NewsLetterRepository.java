package com.dune.greensupermarketbackend.news_letter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsLetterRepository extends JpaRepository<NewsLetterEntity, Integer> {
    Boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
