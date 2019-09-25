package com.train4game.social.service.email;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Email {
    private String from;
    private String to;
    private String subject;
    private String template;
    private Map<String, Object> model;
}
