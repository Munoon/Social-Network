package com.train4game.social.util.exception;

import com.train4game.social.model.Token;
import lombok.Getter;

@Getter
public class TokenExpiredException extends RuntimeException {
    private Token token;

    public TokenExpiredException(String message, Token token) {
        super(message);
        this.token = token;
    }
}
