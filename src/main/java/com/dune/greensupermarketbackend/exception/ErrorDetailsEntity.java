package com.dune.greensupermarketbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailsEntity {
    private LocalDateTime timeStamp;
    private String message;
    private String details;
}
