package com.dune.greensupermarketbackend.news_letter;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="news_letter")
public class NewsLetterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id" , nullable = false, unique = true)
    private Integer id;

    @Column(name="email", nullable = false, unique = true)
    private String email;
}