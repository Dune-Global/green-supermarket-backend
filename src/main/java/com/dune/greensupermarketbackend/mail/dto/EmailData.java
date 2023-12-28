package com.dune.greensupermarketbackend.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailData {
    String to;
    String cc;
    String subject;
    String body;
}
