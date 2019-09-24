package com.train4game.social.util;

import com.train4game.social.model.Token;

import java.time.LocalDateTime;

public class TokenUtil {
    public static boolean isTokenExpired(Token token) {
        return LocalDateTime.now().isAfter(token.getExpirationDate());
    }
}
