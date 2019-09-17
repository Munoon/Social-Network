package com.train4game.social.util.exception;

import com.train4game.social.model.VerificationToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenNotValidException extends RuntimeException {
    private VerificationToken token;
}
